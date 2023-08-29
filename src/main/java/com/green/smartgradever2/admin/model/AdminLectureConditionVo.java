package com.green.smartgradever2.admin.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminLectureConditionVo {
    private Long ilecture;
    private String returnCtnt;
    private LocalDateTime returnDate;
}
