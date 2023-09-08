package com.green.smartgradever2.professor.model;

import com.green.smartgradever2.student.model.StudentScheduleVo;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ProfessorScheduleVo  {
    private LocalTime startTime;
    private LocalTime  endTime;
    private int dayWeek;
    private String lectureName;
}
