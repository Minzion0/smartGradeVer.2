package com.green.smartgradever2.admin.major.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminMajorFindRes {
    private PagingUtils paging;
    private List<AdminMajorVo> vo;
}
