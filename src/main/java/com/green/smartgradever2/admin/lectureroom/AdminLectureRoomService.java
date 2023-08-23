package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLecturRoomFindRes;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
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

    private final AdminLectureRoomRepository LECTURE_ROOM_REP;

    /** 강의실 리스트 INSERT **/
    public Long insLectureRoom(LectureRoomEntity entity) {
        LectureRoomEntity result = LECTURE_ROOM_REP.save(entity);
        if (result == null) {
            return 0L;
        }
        return result.getIlectureRoom();
    }


}
