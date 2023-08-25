package com.green.smartgradever2.admin.student.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data

public class AdminStudentRes {
    private PagingUtils page;
    private List<AdminStudentFindVo>students;

}
