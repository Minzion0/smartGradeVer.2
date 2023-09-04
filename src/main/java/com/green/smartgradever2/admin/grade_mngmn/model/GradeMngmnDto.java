package com.green.smartgradever2.admin.grade_mngmn.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class GradeMngmnDto {
    private Long isemester;
    private Long ilectureName;
    private Long studentNum;
    private int grade;
    private int semester;
    private int size;
    private int staIdx;
    private int page;
    private PagingUtils paging;
    private Pageable pageable;
}
