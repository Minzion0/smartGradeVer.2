package com.green.smartgradever2.settings.security.config.security;


import com.green.smartgradever2.settings.security.config.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//spring security 5.7.0부터 WebSecurityConfigurerAdapter deprecated 됨
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    //webSecurityCustomizer를 제외한 모든 것, 시큐리티를 거친다. 보안과 연관
    /** 권한 설정 **/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authz ->
                            authz.requestMatchers(
                                    "/"
                                    ,"/admin/**"
                                    ,"/api/send-email/**"
                                    ,"/professor/**"
                                    ,"/student/**"
                                   ,"/api/sign-in"
                                    ,"/api/otp-valid"
                                    ,"/api/otp"
                                    ,"/swagger.html"
                                    , "/swagger-ui/**"
                                    , "/v3/api-docs/**"
                                    ,"/imgs/**"
                                    , "/index.html"
                                    ,"/static/**"
                                    ,"/api/student/**"
                                    ,"/api/board/**"
                                    ,"/favicon/**"
                                    ,"/api/forget-password"
                                    ,"/api/change-password"

                            ).permitAll() // 권한 상관없이 전부 사용이 가능하다.
                             .requestMatchers("/api/refresh-token").hasAnyRole("STUDENT", "PROFESSOR", "ADMIN")
                            .requestMatchers("/api/logout").hasAnyRole("STUDENT", "PROFESSOR", "ADMIN")

                            .requestMatchers(HttpMethod.GET, "/api/refresh-token").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/lectureroom/list").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/major/list").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/board/**").permitAll()
                                    .requestMatchers(HttpMethod.GET,"/api/professor/lecture/room").hasAnyRole("ADMIN","PROFESSOR")
                                    .requestMatchers("**exception**").permitAll()
                                    .requestMatchers("/api/professor/**").hasRole("PROFESSOR") // 권한있는 사람만 필터링 한거다
                                    .requestMatchers("/api/student/**").hasRole("STUDENT") // 권한있는 사람만 필터링 한거다
                            .anyRequest().hasRole("ADMIN") // anyRequest 는 거의 마지막에 작성 되어야함
                ) //사용 권한 체크
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용 X
        .httpBasic(http -> http.disable()) //UI 있는 시큐리티 설정을 비활성화
                .csrf(csrf -> csrf.disable()) //CSRF 보안이 필요 X, 쿠키와 세션을 이용해서 인증을 하고 있기 때문에 발생하는 일, https://kchanguk.tistory.com/197
                .exceptionHandling(except -> {
                    except.accessDeniedHandler(new CustomAccessDeniedHandler()); // 인가부분(권한)
                    except.authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증 부분 로그인이 반려됐을때 어떻게 할 것인가
                })
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisService), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
