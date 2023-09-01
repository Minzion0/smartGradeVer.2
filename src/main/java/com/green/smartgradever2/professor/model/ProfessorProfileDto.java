package com.green.smartgradever2.professor.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfessorProfileDto {
    private Long iprofessor;
    private String majorName;
    private String name;  // 이름
    private GenderEnum gender;  // 성별
    private String pic;  // 사진
    private LocalDate birthDate;  // 생년월일
    private String phone;  // 폰번호
    private String email;  // 이메일
    private String address;  // 주소

}
