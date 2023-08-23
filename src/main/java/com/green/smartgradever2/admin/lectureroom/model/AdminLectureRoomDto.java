package com.green.smartgradever2.admin.lectureroom.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLectureRoomDto {
    private Long ilectureRoom;
    private String buildingName;
    private String lectureRoomName;
    private int maxCapacity;
    private int page;
    private int staIdx;
}
