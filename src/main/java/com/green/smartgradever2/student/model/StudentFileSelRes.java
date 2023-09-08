package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentFileSelRes {
    private StudentProfileVo profile;
    private List<StudentProfileLectureVo> lectureList;
    private String deadline;
}
