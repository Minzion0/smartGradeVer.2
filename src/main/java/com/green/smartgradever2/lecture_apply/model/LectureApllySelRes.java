package com.green.smartgradever2.lecture_apply.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LectureApllySelRes {
    private PagingUtils page;
    private List<LectureAppllySelOneRes> list;
}
