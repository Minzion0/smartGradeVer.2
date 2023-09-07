package com.green.smartgradever2.lecture_apply.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LectureSelAllRes {
    private PagingUtils page;
    private List<LectureApplySelDto> lectureList;
}
