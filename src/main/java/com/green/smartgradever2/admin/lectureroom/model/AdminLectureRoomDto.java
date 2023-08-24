package com.green.smartgradever2.admin.lectureroom.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminLectureRoomDto {
    private Long ilectureRoom;
    private String buildingName;
    private String lectureRoomName;
    private int maxCapacity;
    private int size;
    private int staIdx;
    private List<AdminLectureRoomListVo> lectureRoomList;
    private List<AdminLectureRoomVo> lectureRoom;
}
