package com.green.smartgradever2.admin.student.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminStudentDetailRes {

    private AdminStudentProfileVo profile;
    private List<AdminStudentLectureVo>lectureList;

}
