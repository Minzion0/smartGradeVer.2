package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomListVo;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
import com.green.smartgradever2.entity.LectureRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminLectureRoomRepository extends JpaRepository<LectureRoomEntity, Long> {
    List<AdminLectureRoomListVo> findAllByLectureRoomNameAndBuildingName(String lecturRoomName, String buildingName);

    @Query(value = "SELECT building_name FROM lecture_room l",nativeQuery = true)
    List<AdminLectureRoomVo> findByBuildingName(String entity);
}
