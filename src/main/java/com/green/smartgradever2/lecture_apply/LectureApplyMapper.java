package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.lecture_apply.model.LectureApllyDto;
import com.green.smartgradever2.lecture_apply.model.LectureApllyT;
import com.green.smartgradever2.lecture_apply.model.LectureAppllyInsDto;
import com.green.smartgradever2.lecture_apply.model.LectureAppllySelOneRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LectureApplyMapper {
    int InsApplly(LectureAppllyInsDto dto);

    List<LectureAppllySelOneRes> selLectureApplly(LectureApllyT t);

    int InsDayWeek(List<LectureApllyDto> dto);

    int selAplly();

}
