package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminStudentYearGenderRes {
    private List<AdminStudentYearGenderCountVo> man;
    private List<AdminStudentYearGenderCountVo> female;

}
