package com.green.smartgradever2.admin.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminInsSemesterVo {
    private Long isemester;
    private int semester;
    private LocalDate semesterStrDate;
    private LocalDate semesterEndDate;
    private LocalDate lectureApplyDeadline;
    private int delYn;
}
