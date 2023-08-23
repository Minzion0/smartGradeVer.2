package com.green.smartgradever2.settings.security.config.security;


import com.green.smartgradever2.settings.security.config.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider PROVIDER;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String token = PROVIDER.resolveToken(req, PROVIDER.TOKEN_TYPE);
        log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 추출 token: {}", token);

        log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 유효성 체크 시작");
//        if (token!=null){
//            Authentication auth = PROVIDER.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }

        if(token != null && PROVIDER.isValidateToken(token, PROVIDER.ACCESS_KEY)) {
            Authentication auth = PROVIDER.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 유효성 체크 완료");
        }
//        if(token != null) {
//
//            if (!PROVIDER.isValidateToken(token, PROVIDER.ACCESS_KEY)){
//                res.setStatus(401);
//                res.getWriter();
//            }else {
//                String isLogout = redisService.getValues(token);
//                if (ObjectUtils.isEmpty(isLogout)){
//                    Authentication auth = PROVIDER.getAuthentication(token);
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                    log.info("JwtAuthenticationFilter - doFilterInternal: 토큰 유효성 체크 완료");
//
//                }
//
//            }
//        }

      filterChain.doFilter(req, res);
    }
}
