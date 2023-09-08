package com.green.smartgradever2.settings.security.sign;



import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import com.green.smartgradever2.settings.security.config.security.model.OtpValidParam;
import com.green.smartgradever2.settings.security.config.security.otp.OtpRes;
import com.green.smartgradever2.settings.security.sign.model.SignInParam;
import com.green.smartgradever2.settings.security.sign.model.SignInResultDto;
import com.green.smartgradever2.settings.security.sign.model.SignSelPasswordTrueDto;
import com.green.smartgradever2.settings.security.sign.model.SignUpResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "로그인")
@RequestMapping("/api")
public class SignController {
    private final SignService SERVICE;

    //ApiParam은 문서 자동화를 위한 Swagger에서 쓰이는 어노테이션이고
    //RequestParam은 http 로부터 요청 온 정보를 받아오기 위한 스프링 어노테이션이다.


    //    @PostMapping("/sign-in")   로그인 하자마자 토큰
//    @Operation(summary = "로그인")
//    public SignInResultDto signIn(HttpServletRequest req, @RequestParam String id, @RequestParam String password, @RequestParam String role) throws Exception {
//
//        String ip = req.getRemoteAddr();
//        log.info("[signIn] 로그인을 시도하고 있습니다. id: {}, pw: {}, role: {}, ip: {}", id, password, role, ip);
//
//        SignInResultDto dto = SERVICE.signIn(id, password, ip, role);
//        if(dto.getCode() == CommonRes.SUCCESS.getCode()) {
//            log.info("[signIn] 정상적으로 로그인 되었습니다. id: {}, token: {}", id, dto.getAccessToken());
//        }
//        return dto;
//    }
    @PostMapping("/sign-in")
    @Operation(summary = "로그인")
    public SignInResultDto signIn(HttpServletRequest req, @RequestBody SignInParam param) throws Exception {
        String ip = req.getRemoteAddr();
        log.info("[signIn] 로그인을 시도하고 있습니다. id: {}, pw: {}, role: {}, ip: {}", param.getId(), param.getPassword(), param.getRole(), ip);

        SignInResultDto dto = SERVICE.signIn(param);

        return dto;

    }


    @GetMapping("/refresh-token")
    @Operation(summary = "토큰발행")
    public ResponseEntity<SignUpResultDto> refreshToken(HttpServletRequest req,
                                                        @RequestParam String refreshToken) {

        SignUpResultDto dto = SERVICE.refreshToken(req, refreshToken);

        return dto == null ? ResponseEntity.status(405).body(null) : ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> logout(HttpServletRequest req) {
        SERVICE.logout(req);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/otp")
    @Operation(summary = "otp 등록 어플에 등록", description = "role : ROLE_ 기본 관리자 : ADMIN ,학생 : STUDENT , 교수 : PROFESSOR" +
            "<br>iuser : 여기엔 관리자인 경우 pk 교수 및 학생은 학번" +
            "<br>\"barcodeUrl : qr코드 주소")
    public ResponseEntity<OtpRes> otp(@AuthenticationPrincipal MyUserDetails details) throws Exception {

        Long iuser = details.getIuser();
        String result = String.valueOf(iuser);

        String role = details.getRoles().get(0);

        OtpRes res = SERVICE.otp(result, role, iuser);

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/otp-valid")
    @Operation(summary = "otp 인증", description = "otpNum : otp번호" +
            "role : ROLE_ 기본 관리자 : ADMIN ,학생 : STUDENT , 교수 : PROFESSOR" +
            "<br>iuser : 여기엔 관리자인 경우 pk 교수 및 학생은 학번")
    public ResponseEntity<?> otpValid(HttpServletRequest req, @RequestBody OtpValidParam param) throws Exception {

        SignInResultDto otpe = SERVICE.otpValid(req, param);

        return ResponseEntity.ok().body(otpe);
    }

    @PutMapping("/forget-password")
    @Operation(summary = "비밀번호 찾기(변경) 아이디와 OTP 확인")
    public ResponseEntity<?> updForgetPassword(String uid, String role, String inputCode) {
        boolean result = SERVICE.updForgetPassword(uid, role, inputCode);
        return result ?  ResponseEntity.ok().body(result) : ResponseEntity.status(405).body(result);
    }

    @PutMapping("/change-password")
    @Operation(summary = "비밀번호 변경")
    public String updPasswordNew(@RequestBody SignSelPasswordTrueDto dto) {
        return SERVICE.updPasswordNew(dto);
    }


}
