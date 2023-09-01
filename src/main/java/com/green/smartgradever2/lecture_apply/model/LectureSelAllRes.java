package com.green.smartgradever2.lecture_apply.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LectureSelAllRes {
    private List<LectureApplySelDto> lectureList;
}
