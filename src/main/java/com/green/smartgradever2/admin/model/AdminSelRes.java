package com.green.smartgradever2.admin.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminSelRes {
    private List<AdminSelLectureRes>lectures;
    private PagingUtils page;
}
