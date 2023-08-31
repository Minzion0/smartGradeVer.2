package com.green.smartgradever2.student.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudentProFileRes {
    private StudentProfileLectureVo profileDto;
    private List<StudentLectureDto> dtos;
}
