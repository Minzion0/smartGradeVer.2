package com.green.smartgradever2.email;

import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.email.model.EmailDto;
import com.green.smartgradever2.email.model.EmailParam;
import com.green.smartgradever2.email.model.EmailVo;
import com.green.smartgradever2.student.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sendEmail")
@RequiredArgsConstructor
@Tag(name = "이메일 전송")
public class EmailController {

    private final EmailService SERVICE;

    @PostMapping
    @Operation(summary = "이메일 전송")
    public String sendMail(@RequestBody EmailParam param ) throws Exception {

        EmailDto dto = new EmailDto();
        dto.setId(param.getId());
        dto.setPw(param.getPw());
        dto.setRole(param.getRole());

        SERVICE.sendMail(dto);

        return "Email Send Succeed";
    }

}
