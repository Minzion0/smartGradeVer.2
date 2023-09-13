package com.green.smartgradever2.email;

import com.green.smartgradever2.email.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity checkEmail(@RequestBody CheckEmailParam param, @AuthenticationPrincipal MyUserDetails details) {
        CheckEmailDto dto = new CheckEmailDto();
        dto.setIuser(details.getIuser());
        dto.setRole(details.getRoles().get(0));
        dto.setMail(param.getMail());
        return SERVICE.checkEmail(dto);
    }


    @GetMapping("/check-button")
    @Operation(summary = "이메일 인증 확인 버튼 api 백엔드 내부 사용용")
    public String checkApi(HttpServletRequest request) {

       Long iuser = Long.valueOf(request.getParameter("iuser"));
       String role = request.getParameter("role");

        return SERVICE.checkApi(iuser, role);
    }

    @GetMapping("/email-success")
    @Operation(summary = "이메일 인증 완료 됐는지 최종 확인 Api")
    public boolean emailSuccessCheck(@AuthenticationPrincipal MyUserDetails details) throws Exception {
        EmailSuccessCheckDto dto = new EmailSuccessCheckDto();
        dto.setIuser(details.getIuser());
        dto.setRole(details.getRoles().get(0));
        return SERVICE.emailSuccessCheck(dto);
    }
}
