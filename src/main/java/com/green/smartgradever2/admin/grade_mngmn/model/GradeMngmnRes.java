package com.green.smartgradever2.admin.grade_mngmn.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeMngmnRes {
    private PagingUtils paging;
    private GradeMngmnStudentVo student;
    private List<GradeMngmnVo> voList;
    private List<GradeMngmnAvgVo> avgVo;
}
