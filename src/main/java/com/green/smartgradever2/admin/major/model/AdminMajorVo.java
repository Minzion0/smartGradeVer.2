package com.green.smartgradever2.admin.major.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminMajorVo {
    private Long imajor;
    private String majorName;
    private int graduationScore;
    private int delYn;
    private String remarks;
}
