package com.green.smartgradever2.student.model;

import lombok.Data;

@Data
public class StudentUpdDto {
    private String phone;
    private String email;
    private String address;
    private Long studentNum;
}
