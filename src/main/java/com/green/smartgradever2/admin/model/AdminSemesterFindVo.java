package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AdminSemesterFindVo {
    private Long isemester;
    private int year;
    private int semester;
    private LocalDate semesterStrDate;
    private LocalDate semesterEndDate;
    private LocalDate lectureApplyDeadline;
}
