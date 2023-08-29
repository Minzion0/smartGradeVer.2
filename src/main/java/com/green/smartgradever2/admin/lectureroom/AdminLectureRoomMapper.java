package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomDto;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomListVo;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminLectureRoomMapper {
    List<AdminLectureRoomVo> selBuildingName(AdminLectureRoomDto dto);
    List<AdminLectureRoomListVo> selLectureRoom(AdminLectureRoomDto dto);
    int countLectureRoom();

}
