package com.green.smartgradever2.admin.professor.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class AdminProfessorFindRes {
    private PagingUtils page;
    private List<AdminProfessorFindVo>professors;
}
