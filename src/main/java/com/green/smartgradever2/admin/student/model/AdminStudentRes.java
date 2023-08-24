package com.green.smartgradever2.admin.student.model;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
public class AdminStudentRes {
    private Pageable page;
    private List<AdminStudentFindVo>students;

}
