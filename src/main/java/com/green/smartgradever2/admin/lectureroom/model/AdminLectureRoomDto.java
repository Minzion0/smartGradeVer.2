package com.green.smartgradever2.admin.lectureroom.model;

import com.green.smartgradever2.utils.PagingUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private int page;
    private PagingUtils paging;
    private Pageable pageable;
    private List<AdminLectureRoomListVo> lectureRoomList;
    private List<AdminLectureRoomVo> lectureRoom;
    private AdminLectureRoomFindRes findRes;
}
