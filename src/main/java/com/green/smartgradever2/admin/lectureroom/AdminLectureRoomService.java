package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomFindRes;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomDto;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomListVo;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
import com.green.smartgradever2.entity.LectureRoomEntity;
import jakarta.persistence.EntityNotFoundException;
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


    /** 강의실 리스트 INSERT **/
    public Long insLectureRoom(LectureRoomEntity entity) {
        LectureRoomEntity result = LECTURE_ROOM_REP.save(entity);
        if (result == null) {
            return 0L;
        }
        return result.getIlectureRoom();
    }

    /** 강의실 리스트 SELECT **/
    public AdminLectureRoomFindRes selLectureRoom(LectureRoomEntity entity, Pageable pageable) {
        List<AdminLectureRoomListVo> list = LECTURE_ROOM_REP.findAllByLectureRoomNameAndBuildingName(entity.getLectureRoomName(), entity.getBuildingName());
        List<AdminLectureRoomVo> buildingList = LECTURE_ROOM_REP.findByBuildingName(entity.getBuildingName());


        AdminLectureRoomDto dto = AdminLectureRoomDto.builder()
                .lectureRoom(buildingList)
                .lectureRoomList(list)
                .staIdx((pageable.getPageNumber() - 1) * pageable.getPageSize())
                .size(pageable.getPageSize())
                .build();

        return AdminLectureRoomFindRes.builder()
                .lectureRoomList(dto.getLectureRoomList())
                .lectureRoom(dto.getLectureRoom())
                .staIdx(dto.getStaIdx())
                .size(dto.getSize())
                .build();
    }

    /**
     * 강의실 DELETE ( del_yn 0 1 변경 )
     **/
    public AdminLectureRoomListVo delLectureRoom(LectureRoomEntity entity) {
        Optional<LectureRoomEntity> byId = LECTURE_ROOM_REP.findById(entity.getIlectureRoom());

        if (byId.isPresent()) {
            LectureRoomEntity lectureRoomEntity = byId.get();
            lectureRoomEntity.setDelYn(1);

            LectureRoomEntity save = LECTURE_ROOM_REP.save(lectureRoomEntity);
            return AdminLectureRoomListVo.builder()
                    .delYn(save.getDelYn())
                    .build();
        } else {
            throw new EntityNotFoundException("not found");
        }

    }


}
