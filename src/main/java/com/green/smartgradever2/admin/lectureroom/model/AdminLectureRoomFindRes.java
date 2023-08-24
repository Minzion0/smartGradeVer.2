package com.green.smartgradever2.admin.lectureroom.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class AdminLectureRoomFindRes {
    private PagingUtils paging;
    private List<AdminLectureRoomVo> lectureRoom;
    private List<AdminLectureRoomListVo> lectureRoomList;
    private int size;
    private int staIdx;
}
