package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradeMngmnMapper {
    int countStudent();

    List<GradeMngmnVo> selGradeFindStudent(GradeMngmnDto dto);

    List<GradeMngmnAvgVo> GradeMngmnAvg(GradeMngmnDto dto);

    GradeMngmnStudentVo selGradeMngmnStudent(GradeMngmnDto dto);

    GradeMngmnDetailVo selGradeFindStudentDetail(GradeMngmnDetailSelDto dto);

    int insGradeMngmn(GradeMngmnInsDto dto);

    int updAvgScore(GradeMngmnUpdDto dto);

    GradeAvgVo selAvgScore(GradeMngmnUpdDto dto);
}
