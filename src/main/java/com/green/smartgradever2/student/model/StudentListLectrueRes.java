package com.green.smartgradever2.student.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudentListLectrueRes {
    private PagingUtils page;
    private List<StudentListLectureVo> lectureList;
}
