package com.green.smartgradever2.admin.major.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AdminMajorFindRes {
    private PagingUtils page;
    private List<AdminMajorVo> vo;
}
