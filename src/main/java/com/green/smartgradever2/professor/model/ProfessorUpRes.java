package com.green.smartgradever2.professor.model;

import com.green.smartgradever2.config.entity.ProfessorEntity;
import lombok.Data;

@Data
public class ProfessorUpRes {
    private Long iprofessor;
    private String address;
    private String phone;
    private String email;
    private String pic;

    public ProfessorUpRes(ProfessorEntity professor) {
        this.iprofessor = professor.getIprofessor();
        this.address = professor.getAddress();
        this.phone = professor.getPhone();
        this.email = professor.getEmail();
        this.pic = professor.getPic();
    }
}
