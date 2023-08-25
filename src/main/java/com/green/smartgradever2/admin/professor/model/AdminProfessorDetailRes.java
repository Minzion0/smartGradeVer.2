package com.green.smartgradever2.admin.professor.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminProfessorDetailRes {
    private List<AdminProfessorLectureVo> lectureList;
    private AdminProfessorProfileVo profile;

}
