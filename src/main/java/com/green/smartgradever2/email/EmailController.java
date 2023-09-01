package com.green.smartgradever2.email;

import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.email.model.EmailDto;
import com.green.smartgradever2.email.model.EmailParam;
import com.green.smartgradever2.email.model.EmailVo;
import com.green.smartgradever2.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sendEmail")
@RequiredArgsConstructor
public class EmailController {

    private final StudentRepository STUDENT_REP;
    private final EmailService SERVICE;

    @PostMapping
    public EmailVo sendMail( EmailParam param ) throws Exception {

        StudentEntity entity = STUDENT_REP.findById(param.getId()).get();

        EmailDto dto = new EmailDto();
        dto.setId(param.getId());
        dto.setPw(param.getPw());
        dto.setRole(param.getRole());
        dto.setSecretKey(entity.getSecretKey());
        dto.setOptImg(entity.getOtpUrl());

        SERVICE.sendMail(dto);

        return EmailVo.builder()
                .id(dto.getId())
                .role(dto.getRole())
                .secretKey(dto.getSecretKey())
                .optUrl(dto.getOptImg())
                .build();
    }

}
