package com.green.smartgradever2.admin.major.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMajorVo {
    private Long imajor;
    private String majorName;
    private int graduationScore;
    private int delYn;
    private String remarks;
}
