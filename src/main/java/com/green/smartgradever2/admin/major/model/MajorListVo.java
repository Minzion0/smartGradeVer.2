package com.green.smartgradever2.admin.major.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MajorListVo {
    private Long imajor;
    private String majorName;
}
