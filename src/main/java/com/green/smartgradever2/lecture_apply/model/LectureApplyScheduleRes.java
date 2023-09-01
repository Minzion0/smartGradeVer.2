package com.green.smartgradever2.lecture_apply.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LectureApplyScheduleRes {
    private Long ilectureRoom;
    private String buildingName;
    private String lectureRoomName;
    private List<LectureApplyScheduleVo> schedule;

}
