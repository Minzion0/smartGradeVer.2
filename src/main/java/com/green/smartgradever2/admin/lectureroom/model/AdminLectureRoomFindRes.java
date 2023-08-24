package com.green.smartgradever2.admin.lectureroom.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class AdminLectureRoomFindRes {
    private List<AdminLectureRoomVo> lectureRoom;
    private List<AdminLectureRoomListVo> lectureRoomList;
    private int size;
    private int staIdx;
}
