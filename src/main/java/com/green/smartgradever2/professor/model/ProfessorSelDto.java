package com.green.smartgradever2.professor.model;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfessorSelDto {
    private Long iprofessor;
    private Long imajor;  // 전공의 id 정보
    private String nm;  // 이름
    private GenderEnum gender;  // 성별
    private String pic;  // 사진
    private LocalDate birthDate;  // 생년월일
    private String phone;  // 폰번호
    private String email;  // 이메일
    private String address;  // 주소
    private String role;  // 롤값
}
