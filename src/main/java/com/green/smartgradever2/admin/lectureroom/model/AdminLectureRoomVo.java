package com.green.smartgradever2.admin.lectureroom.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AdminLectureRoomVo {
    private Long ilectureRoom;
    private String lectureRoomName;
    private String buildingName;
    private int maxCapacity;
    private int delYn;
}
