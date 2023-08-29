package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLectureNameFindVo {
    private Long ilectureName;
    private String lectureName;
    private int score;
    private int delYn;
}
