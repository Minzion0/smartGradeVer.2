package com.green.smartgradever2.student.model;

import com.green.smartgradever2.config.entity.StudentEntity;
import lombok.Data;

@Data
public class StudentUpRes {
    private Long studentNum;
    private String address;
    private String phone;
    private String email;
    private String pic;

    public StudentUpRes(StudentEntity entity) {
        this.studentNum = entity.getStudentNum();
        this.address = entity.getAddress();
        this.phone = entity.getPhone();
        this.email = entity.getEmail();
        this.pic = entity.getPic();
    }
}
