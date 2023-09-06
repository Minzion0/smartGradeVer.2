package com.green.smartgradever2.student.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudentHistoryRes {
    private PagingUtils page;
    List<StudentHistoryDto> lectureList;
}
