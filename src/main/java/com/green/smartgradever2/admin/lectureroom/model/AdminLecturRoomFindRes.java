package com.green.smartgradever2.admin.lectureroom.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminLecturRoomFindRes {
    private List<AdminLectureRoomListVo> lectureRoomList;
    private List<AdminLectureRoomVo> lectureRoom;
}
