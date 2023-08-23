package com.green.smartgradever2.settings.security.config.security;


import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component // 이게 빈등록
@Slf4j
public class JwtTokenProvider {

    public final Key ACCESS_KEY;
    public final Key REFRESH_KEY;
    public final String TOKEN_TYPE;
    public final long ACCESS_TOKEN_VALID_MS = 3_600_000L; // 1000L * 60 * 60 -> 1시간
//    public final long ACCESS_TOKEN_VALID_MS = 60_000L; // 1분
    public final long REFRESH_TOKEN_VALID_MS = 1_296_000_000L; // 1000L * 60 * 60 * 24 * 15 -> 15일

    // application.yml에서 주입받은 secret 값을 base64 decode하여 key 변수에 할당
    public JwtTokenProvider(@Value("${springboot.jwt.access-secret}") String accessSecretKey
                            , @Value("${springboot.jwt.refresh-secret}") String refreshSecretKey
                            , @Value("${springboot.jwt.token-type}") String tokenType) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
        this.ACCESS_KEY = Keys.hmacShaKeyFor(accessKeyBytes);

        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.REFRESH_KEY = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.TOKEN_TYPE = tokenType;
    }

    // Authentication 객체에 포함되어 있는 권한 정보들을 담은 토큰을 생성
    public String generateJwtToken(String strIuser, List<String> roles, long token_valid_ms, Key key) {
        log.info("JwtTokenProvider - generateJwtToken: 토큰 생성 시작");
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(createClaims(strIuser, roles)) // 내가 지금 담을 내용들 지금은 pk값이랑 권한정보 담음
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + token_valid_ms))
                .signWith(key)
                .compact();
        log.info("JwtTokenProvider - generateJwtToken: 토큰 생성 완료");
        return token;
    }



    private Claims createClaims(String strIuser, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(strIuser);
        claims.put("roles", roles);
        return claims;
    }

    // JWT토큰을 복호화하여 토큰에 들어있는 정보를 꺼냅니다.
    public Authentication getAuthentication (String token) {
        log.info("JwtTokenProvider - getAuthentication: 토큰 인증 정보 조회 시작");
        //UserDetails userDetails = SERVICE.loadUserByUsername(getUsername(token));
        UserDetails userDetails = getUserDetailsFromToken(token, ACCESS_KEY);
        log.info("JwtTokenProvider - getAuthentication: 토큰 인증 정보 조회 완료, UserDetails UserName : {}"
                , userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    // UserDetails 객체를 만들어서 복호화된 User정보 값 리턴
    private UserDetails getUserDetailsFromToken(String token, Key key) {
        Claims claims = getClaims(token, key);
        String strIuser = claims.getSubject();
        List<String> roles = (List<String>)claims.get("roles");
        return MyUserDetails
                .builder()
                .iuser(Long.valueOf(strIuser))
                .roles(roles)
                .build();
    }

    public String resolveToken(HttpServletRequest req, String type) {
        log.info("JwtTokenProvider - resolveToken: HTTP 헤더에서 Token 값 추출");
        String headerAuth = req.getHeader("Authorization");
        return headerAuth == null ? null : headerAuth.substring(type.length()).trim();
    }

    public Claims getClaims(String token, Key key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidateToken(String token, Key key) {
        log.info("JwtTokenProvider - isValidateToken: 토큰 유효 체크 시작");
        try {
            return !getClaims(token, key).getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("JwtTokenProvider - isValidateToken: 토큰 유효 체크 예외 발생");
            return false;
        }
        // 만료시간이 현재시간보다 지났으면 true > 리턴값 : false;
        // 만료시간이 현재시간보다 안 지났으면 false > 리턴값 : true;
    }

    public long getTokenExpirationTime(String token, Key key) {
        try {
            return getClaims(token, key).getExpiration().getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }
}
