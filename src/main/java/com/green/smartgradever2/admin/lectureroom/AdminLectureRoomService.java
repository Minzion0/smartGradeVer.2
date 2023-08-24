package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLecturRoomFindRes;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomDto;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomListVo;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
import com.green.smartgradever2.entity.LectureRoomEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminLectureRoomService {

    private final AdminLectureRoomRepository LECTURE_ROOM_REP;

    private final EntityManager em;

    /** 강의실 리스트 INSERT **/
    public Long insLectureRoom(LectureRoomEntity entity) {
        LectureRoomEntity result = LECTURE_ROOM_REP.save(entity);
        if (result == null) {
            return 0L;
        }
        return result.getIlectureRoom();
    }

    /** 강의실 리스트 SELECT **/
    public AdminLecturRoomFindRes selLectureRoom(LectureRoomEntity entity, Pageable pageable) {
        List<AdminLectureRoomListVo> list = LECTURE_ROOM_REP.findAllByLectureRoomNameAndBuildingName(entity.getLectureRoomName(), entity.getBuildingName());
        List<AdminLectureRoomVo> buildingList = LECTURE_ROOM_REP.findByBuildingName(entity.getBuildingName());


        AdminLectureRoomDto dto = AdminLectureRoomDto.builder()
                .lectureRoom(buildingList)
                .lectureRoomList(list)
                .staIdx((pageable.getPageNumber() - 1) * pageable.getPageSize())
                .size(pageable.getPageSize())
                .build();

        return AdminLecturRoomFindRes.builder()
                .lectureRoomList(dto.getLectureRoomList())
                .lectureRoom(dto.getLectureRoom())
                .staIdx(dto.getStaIdx())
                .size(dto.getSize())
                .build();
    }

    /**
     * 강의실 DELETE ( del_yn 0 1 변경 )
     **/
    public Long delLectureRoom(LectureRoomEntity entity) {
        if (LECTURE_ROOM_REP.findById(entity.getIlectureRoom()).isEmpty()) {
            return 0L;
        } else {
            LECTURE_ROOM_REP.save(entity.builder().delYn(1).build());
            return 1L;
        }
    }


}
