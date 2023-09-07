package com.green.smartgradever2.admin.lectureroom.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminLectureRoomFindRes {
    private PagingUtils page;
    private List<AdminLectureRoomVo> lectureRoom;
    private List<AdminLectureRoomListVo> lectureRoomList;
}
