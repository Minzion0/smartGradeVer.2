package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.*;
import com.green.smartgradever2.config.entity.LectureRoomEntity;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminLectureRoomService {

    private final AdminLectureRoomRepository LECTURE_ROOM_REP;
    private final AdminLectureRoomMapper MAPPER;
    private final AdminLectureRoomQdsl adminLectureRoomQdsl;


    /** 강의실 INSERT **/
    public Long insLectureRoom(AdminLectureRoomInsDto dto) {
        LectureRoomEntity entity = new LectureRoomEntity();
        entity.setLectureRoomName(dto.getLectureRoomName());
        entity.setBuildingName(dto.getBuildingName());
        entity.setMaxCapacity(dto.getMaxCapacity());
        LectureRoomEntity result = LECTURE_ROOM_REP.save(entity);
        if (result == null) {
            return 0L;
        }
        return result.getIlectureRoom();
    }

    /** 강의실 리스트 SELECT **/
    public AdminLectureRoomFindRes selLectureRoom(AdminLectureRoomDto dto, Pageable pageable) {
        long maxPage = LECTURE_ROOM_REP.count();
        PagingUtils utils = new PagingUtils(dto.getPage(),(int)maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<AdminLectureRoomListVo> voList = adminLectureRoomQdsl.voList(dto, pageable);
        List<AdminLectureRoomVo> vo = adminLectureRoomQdsl.vo();

        return AdminLectureRoomFindRes.builder()
                .lectureRoomList(voList)
                .lectureRoom(vo)
                .page(utils)
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

    public List<LectureRoomVo> getLectureRoomList(){
        List<LectureRoomEntity> all = LECTURE_ROOM_REP.findAll();
        return all.stream().map(list-> LectureRoomVo.builder()
                .ilectureRoom(list.getIlectureRoom())
                .lectureRoomName(list.getBuildingName()+" "+list.getLectureRoomName())
                .maxCapacity(list.getMaxCapacity())
                .build()).toList();
    }


}
