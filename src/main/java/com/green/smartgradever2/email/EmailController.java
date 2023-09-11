package com.green.smartgradever2.email;

import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.email.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import com.green.smartgradever2.student.StudentRepository;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/send-email")
@RequiredArgsConstructor
@Tag(name = "이메일 전송")
public class EmailController {

    private final EmailService SERVICE;

    @PostMapping
    @Operation(summary = "OTP 이메일 전송")
    public String sendMail(@RequestBody EmailParam param ) throws Exception {

        EmailDto dto = new EmailDto();
        dto.setId(param.getId());
        dto.setPw(param.getPw());
        dto.setRole(param.getRole());

        SERVICE.sendMail(dto);

        return "Email Send Succeed";
    }

    @PostMapping("/check-mail")
    @Operation(summary = "이메일 인증 전송 버튼")
    public ResponseEntity checkEmail(@RequestBody CheckEmailParam param, @AuthenticationPrincipal MyUserDetails details, HttpServletRequest request) {

        // Authorization 헤더를 가져옵니다.
        String authorizationHeader = request.getHeader("Authorization");

        // Authorization 헤더의 값을 분리합니다.
        String[] authorizationHeaderParts = authorizationHeader.split(" ");

        // 토큰을 가져옵니다.
        String token = authorizationHeaderParts[1];

        CheckEmailDto dto = new CheckEmailDto();
        dto.setIuser(details.getIuser());
        dto.setRole(details.getRoles().get(0));
        dto.setMail(param.getMail());
        return SERVICE.checkEmail(dto, token);
    }


    @GetMapping("/check-button")
    @Operation(summary = "이메일 인증 확인 버튼 api 백엔드 내부 사용용")
    public String checkApi(HttpServletRequest request) throws Exception{

        String token = request.getHeader("Authorization");
        token = URLDecoder.decode(token, "UTF-8");

        Long userId = Long.valueOf(Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody().getSubject());
        String role = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody().get("role").toString();

        return SERVICE.checkApi(userId, role);

    }
}
