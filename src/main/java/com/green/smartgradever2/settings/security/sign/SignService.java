package com.green.smartgradever2.settings.security.sign;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.professor.ProfessorRepository;
import com.green.smartgradever2.settings.security.CommonRes;
import com.green.smartgradever2.settings.security.config.RedisService;
import com.green.smartgradever2.settings.security.config.security.AuthenticationFacade;
import com.green.smartgradever2.settings.security.config.security.JwtTokenProvider;
import com.green.smartgradever2.settings.security.config.security.UserDetailsMapper;
import com.green.smartgradever2.settings.security.config.security.model.*;
import com.green.smartgradever2.settings.security.config.security.otp.OtpRes;
import com.green.smartgradever2.settings.security.config.security.otp.TOTP;
import com.green.smartgradever2.settings.security.config.security.otp.TOTPTokenGenerator;
import com.green.smartgradever2.settings.security.sign.model.*;
import com.green.smartgradever2.student.StudentRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignService {
    private final UserDetailsMapper MAPPER;
    private final JwtTokenProvider JWT_PROVIDER;
    private final PasswordEncoder PW_ENCODER;
    private final RedisService REDIS_SERVICE;
    private final AuthenticationFacade FACADE;
    private final ObjectMapper OBJECT_MAPPER;
    private final TOTPTokenGenerator totp;
    private final StudentRepository STUDENT_REP;
    private final ProfessorRepository PROFESSOR_REP;


    /** 로그인 **/
    public SignInResultDto signIn(SignInParam param) throws Exception {

        SignInResultDto dto = SignInResultDto.builder().build();
        log.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        String id = param.getId();
        String password = param.getPassword();
        String role = param.getRole();
        UserEntity user = MAPPER.getByUid(id, role);

        log.info("[getSignInResult] id: {}", id);
        log.info("[getSignInResult] 패스워드 비교 password :{}", password);
        log.info("[getSignInResult] 패스워드 비교 password :{}", PW_ENCODER.encode(password));
        if (!PW_ENCODER.matches(password, user.getUpw())) {
            log.info("비밀번호 다름");
            //throw new RuntimeException("비밀번호 다름");
            setFileResult(dto);
            return dto;
        }
        log.info("[getSignInResult] 패스워드 일치");

        setSuccessResult(dto);

        if (role.equals("ROLE_ADMIN") || user.getSecretKey() == null) {
            dto = issueToken(user.getUid(), role);
            dto.setSecretKey(false);

        }

        return dto;

    }

    /** 리프레쉬 토큰 발행 **/
    public SignInResultDto refreshToken(HttpServletRequest req, String refreshToken) {
        if (!(JWT_PROVIDER.isValidateToken(refreshToken, JWT_PROVIDER.REFRESH_KEY))) {
            return null;
        }

        Claims claims = null;
        try {
            claims = JWT_PROVIDER.getClaims(refreshToken, JWT_PROVIDER.REFRESH_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (claims == null) {
            return null;
        }

        String strIuser = claims.getSubject();
        Long iuser = Long.valueOf(strIuser);
        String ip = req.getRemoteAddr();

        String redisKey = String.format("b:RT(%s):%s:%s", "Server", iuser, ip);
        String redisRt = REDIS_SERVICE.getValues(redisKey);
        if (redisRt == null) { // Redis에 저장되어 있는 RT가 없을 경우
            return null; // -> 재로그인 요청
        }
        try {
            if (!redisRt.equals(refreshToken)) {
                return null;
            }

            List<String> roles = (List<String>) claims.get("roles");
            String reAccessToken = JWT_PROVIDER.generateJwtToken(strIuser, roles,JWT_PROVIDER.ACCESS_TOKEN_VALID_MS, JWT_PROVIDER.ACCESS_KEY);
            SignInResultDto dto = SignInResultDto.builder()
                    .accessToken(reAccessToken)
                    .refreshToken(refreshToken)
                    .build();
                    dto.setSuccess(true);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 로그인 성공 시 **/
    private void setSuccessResult(SignUpResultDto result) {
        result.setSuccess(true);
        result.setSecretKey(true);
        result.setCode(CommonRes.SUCCESS.getCode());
        result.setMsg(CommonRes.SUCCESS.getMsg());
    }

    private void setFileResult(SignUpResultDto result) {
        result.setSuccess(false);
        result.setCode(CommonRes.FAIL.getCode());
        result.setMsg(CommonRes.FAIL.getMsg());
    }

    /** 로그아웃 **/
    public void logout(HttpServletRequest req) {

        String accessToken = JWT_PROVIDER.resolveToken(req, JWT_PROVIDER.TOKEN_TYPE);
        Long iuser = FACADE.getLoginUserPk();
        String ip = req.getRemoteAddr();

        // Redis에 저장되어 있는 RT 삭제
        String redisKey = String.format("b:RT(%s):%s:%s", "Server", iuser, ip);
        String refreshTokenInRedis = REDIS_SERVICE.getValues(redisKey);
        if (refreshTokenInRedis != null) {
            REDIS_SERVICE.deleteValues(redisKey);
        }

        // Redis에 로그아웃 처리한 AT 저장
        long expiration = JWT_PROVIDER.getTokenExpirationTime(accessToken, JWT_PROVIDER.ACCESS_KEY) - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        REDIS_SERVICE.setValuesWithTimeout(accessToken, "logout", expiration);  //남은시간 이후가 되면 삭제가 되도록 함.
    }

    /** OTP 발행 및 DB OPT 큐알 이미지 저장 **/
    public OtpRes otp(String uid, String role, Long iuser) throws Exception {
        ProfessorEntity professor = null;
        StudentEntity student = null;

        String secretKey = totp.generateSecretKey();//설정할 secretKey를 생성
        UserSelRoleEmailVo vo = MAPPER.getUserRoleEmail(uid, role);
        String email = vo.getEmail();
        if (email == null) {
            throw new Exception("이메일 등록이 필요합니다");
        }

        String issuer = "GreenUniversity";
        String account = email;
        String barcodeUrl = totp.getGoogleAuthenticatorBarcode(secretKey, account, issuer);
        OtpRes res = OtpRes.builder().barcodeUrl(barcodeUrl).secretKey(secretKey).build();

        log.info("barcodeUrl.substring(22) : {}", barcodeUrl.substring(22));
        String newOptUrl = "https://chart.googleapis.com" + barcodeUrl.substring(22);
        log.info("newOptUrl : {}", newOptUrl);
        try {
            updSecretKey(uid, role, secretKey);
        }catch (Exception e){
            throw new Exception("등록 오류 입니다");
        }
        /** otp 큐얄 저장 **/
        if (role.equals("ROLE_STUDENT")){
            student = STUDENT_REP.findById(iuser).get();
            student.setOtpUrl(newOptUrl);
            STUDENT_REP.save(student);
        } else if (role.equals("ROLE_PROFESSOR")) {
            professor = PROFESSOR_REP.findById(iuser).get();
            professor.setOtpUrl(newOptUrl);
            PROFESSOR_REP.save(professor);
        }
        return res;
    }

    /** OTP 시크릿키 발급 **/
    public int updSecretKey(String uid, String role, String secretKey) throws Exception {
        UserUpdSecretKeyDto dto = new UserUpdSecretKeyDto();
        dto.setRole(role);
        dto.setSecretKey(secretKey);
        dto.setUid(uid);
        int result = MAPPER.updSecretKey(dto);
        if (result == 0) {
            throw new Exception();
        }
        return result;
    }

    /** OTP 확인 등록 **/
    public SignInResultDto otpValid(HttpServletRequest req, OtpValidParam param) throws Exception {
        String ip = req.getRemoteAddr();
        String inputCode = param.getOtpNum();
        String uid = param.getUid();
        String role = param.getRole();

        if ("ROLE_STUDENT".equals(role)) {
            uid = MAPPER.getStudentNum(uid);
        }

        UserSelRoleEmailVo vo = MAPPER.getUserRoleEmail(uid, role);
        String otpCode = getOtpCode(vo.getSecretKey());

        boolean result = otpCode.equals(inputCode);
        if (!result) {
            throw new Exception("유효하지 않은 otp 코드");
        }
        //인증 성공시 토큰 토큰 발행
        return issueToken(uid, role);
    }

    /** OTP 코드 발행 **/
    private String getOtpCode(String secretKey) {

        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);

        return TOTP.getOTP(hexKey);
    }

    private SignInResultDto issueToken(String iuser, String role) throws JsonProcessingException {

        System.out.println("iuser = " + iuser);
        UserEntity user = MAPPER.getByUid(iuser, role);
        if ("ROLE_STUDENT".equals(role)) {
            user = MAPPER.getUserSecret(iuser, role);

        }


        System.out.println("user = " + user);
        String redisKey = String.format("b:RT(%s):%s", "Server", user.getIuser());
        if (REDIS_SERVICE.getValues(redisKey) != null) {
            REDIS_SERVICE.deleteValues(redisKey);
        }

        log.info("[getSignInResult] access_token 객체 생성");
        String accessToken = JWT_PROVIDER.generateJwtToken(String.valueOf(user.getIuser()), Collections.singletonList(user.getRole()), JWT_PROVIDER.ACCESS_TOKEN_VALID_MS, JWT_PROVIDER.ACCESS_KEY);
        String refreshToken = JWT_PROVIDER.generateJwtToken(String.valueOf(user.getIuser()), Collections.singletonList(user.getRole()), JWT_PROVIDER.REFRESH_TOKEN_VALID_MS, JWT_PROVIDER.REFRESH_KEY);

        //Redis에 RT 저장
        RedisJwtVo redisJwtVo = RedisJwtVo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        String value = OBJECT_MAPPER.writeValueAsString(redisJwtVo);
        REDIS_SERVICE.setValues(redisKey, value);

        // 저장할 목록만 저장하기
        UserTokenEntity tokenEntity = UserTokenEntity.builder()
                .iuser(user.getIuser())
                .role(role)
                .build();
        REDIS_SERVICE.setValues(redisKey, refreshToken);


        log.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto dto = SignInResultDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        log.info("[getSignInResult] SignInResultDto 객체 값 주입");
        setSuccessResult(dto);
        return dto;
    }

    /** 비밀번호 변경 **/
    public boolean updForgetPassword(String uid, String role, String inputCode) {
        UserEntity entity = MAPPER.getByUid(uid, role);
        String otpCode = getOtpCode(entity.getSecretKey());
        boolean result = otpCode.equals(inputCode);

        if (result) {
            MAPPER.updForgetPasswordTrue(uid, role);
            return true;
        }
        return false;
    }

    /** 새로운 비밀번호 변경 **/
    public String updPasswordNew(SignSelPasswordTrueDto dto) {
        SignSelPasswordTrueVo result = MAPPER.selTruePassword(dto);

        log.info("result.getUpw() : {}", result.getUpw());
        if (result.getUpw().equals("true")) {
            UpdForgetPasswordDto passwordDto = new UpdForgetPasswordDto();
            String npw = PW_ENCODER.encode(dto.getUpw());
            passwordDto.setUpw(npw);
            passwordDto.setRole(result.getRole());
            passwordDto.setUid(result.getUid());
            MAPPER.updForgetPassword(passwordDto);
            return "비밀번호 변경이 완료되었습니다.";
        }
        return "비밀번호 변경이 실패하였습니다.";
    }
}

