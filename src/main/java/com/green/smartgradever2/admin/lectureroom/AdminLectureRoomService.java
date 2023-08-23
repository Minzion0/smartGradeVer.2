package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLecturRoomFindRes;
import com.green.smartgradever2.entity.LectureRoomEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLectureRoomService {

    private final AdminLectureRoomRepository adminLectureroomRep;

    /** 강의실 리스트 SELECT **/
    public List<AdminLecturRoomFindRes> selLectureRoom(Pageable pageable) {
        List<LectureRoomEntity> all = adminLectureroomRep.findAll();

        return AdminLecturRoomFindRes.builder()
                .lectureRoom()
                .lectureRoomList()
                .build();

    }
}
