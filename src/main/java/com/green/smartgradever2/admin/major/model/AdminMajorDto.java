package com.green.smartgradever2.admin.major.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMajorDto {
    private Long imajor;
    private String majorName;
    private int graduationScore;
    private Integer delYn;
    private String remarks;
    private int size;
    private int staIdx;
    private int page;
    private PagingUtils paging;
    private Pageable pageable;
}
