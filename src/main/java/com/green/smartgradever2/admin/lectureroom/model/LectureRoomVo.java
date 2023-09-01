package com.green.smartgradever2.admin.lectureroom.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LectureRoomVo {
    private Long ilectureRoom;
    private String lectureRoomName;
    private int maxCapacity;
}
