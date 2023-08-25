package com.green.smartgradever2.admin.student.model;

import lombok.Data;

@Data
public class AdminStudentLectureVo {
    private Long ilecture;
    private String lectureName;
    private String  lectureStrDate;
    private String  lectureEndDate;
    private String  lectureStrTime;
    private String  lectureEndTime;
}
