package com.green.smartgradever2.admin.lectureroom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLectureRoomDto {
    private Long ilectureRoom;
    private String buildingName;
    private String lectureRoomName;
    private int maxCapacity;
    private int size;
    private int staIdx;
    private List<AdminLectureRoomListVo> lectureRoomList;
    private List<AdminLectureRoomVo> lectureRoom;
    private AdminLectureRoomFindRes findRes;
}
