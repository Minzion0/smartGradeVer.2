package com.green.smartgradever2.admin.lectureroom.model;

import lombok.Data;

@Data
public class AdminLectureRoomListVo {
    private Long ilectureRoom;
    private String lectureRoomName;
    private String buildingName;
    private int maxCapacity;
    private int delYn;
}
