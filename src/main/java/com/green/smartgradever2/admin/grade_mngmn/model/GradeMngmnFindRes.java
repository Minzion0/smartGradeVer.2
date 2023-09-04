package com.green.smartgradever2.admin.grade_mngmn.model;

import com.green.smartgradever2.config.entity.StudentSemesterScoreEntity;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeMngmnFindRes {
    private PagingUtils paging;
    private GradeMngmnStudentVo student;
    private List<GradeMngmnVo> voList;
    private List<GradeMngmnAvgVo> avgVo;
}
