package com.green.smartgradever2.student.model;

import lombok.Data;

@Data
public class StudentUpdPasswordDto {
    private Long istudent;
    private String studentPassword;
    private String role;
}
