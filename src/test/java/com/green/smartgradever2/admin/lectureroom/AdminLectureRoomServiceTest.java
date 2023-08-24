package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomFindRes;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomListVo;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
import com.green.smartgradever2.entity.LectureRoomEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Import({AdminLectureRoomService.class})
class AdminLectureRoomServiceTest {

    @Autowired
    private AdminLectureRoomService SERVICE;

    @MockBean
    private AdminLectureRoomRepository REP;

    @MockBean
    private AdminLectureRoomMapper MAPPER;

    @MockBean
    private Pageable pageable;

    @Test
    void insLectureRoom() {
        LectureRoomEntity entity = new LectureRoomEntity();
        entity.setLectureRoomName("502호");
        entity.setBuildingName("백매관");
        entity.setMaxCapacity(30);
        entity.setIlectureRoom(1L);

        when(REP.save(any())).thenReturn(entity);

        Long result = SERVICE.insLectureRoom(entity);

        assertEquals(result, 1);

    }

    @Test
    void selLectureRoom() {
        LectureRoomEntity entity = new LectureRoomEntity();
        List<AdminLectureRoomListVo> voList = new ArrayList<>();
        List<AdminLectureRoomVo> vo = new ArrayList<>();
        voList.add(new AdminLectureRoomListVo(1L, "502호", "백매관", 30, 0));

        when(REP.findAllByLectureRoomNameAndBuildingName(any(),any())).thenReturn(voList);
        when(REP.findByBuildingName(any())).thenReturn(vo);

        AdminLectureRoomFindRes res = SERVICE.selLectureRoom(entity, pageable);

        assertEquals(voList.get(0).getIlectureRoom(), res.getLectureRoomList().get(0).getIlectureRoom());
        assertEquals(voList.get(0).getLectureRoomName(), res.getLectureRoomList().get(0).getLectureRoomName());
        assertEquals(voList.get(0).getMaxCapacity(), res.getLectureRoomList().get(0).getMaxCapacity());

        verify(REP).findAllByLectureRoomNameAndBuildingName(any(),any());
        verify(REP).findByBuildingName(any());

    }

    @Test
    void delLectureRoom() throws EntityNotFoundException{
        LectureRoomEntity entity = new LectureRoomEntity();
        AdminLectureRoomListVo vo = new AdminLectureRoomListVo();
        entity.setDelYn(1);
        entity.setIlectureRoom(1L);

        when(REP.save(any())).thenReturn(1);

        AdminLectureRoomListVo res = SERVICE.delLectureRoom(entity);

        assertEquals(res.getDelYn(), entity.getDelYn());

        verify(REP).save(any());

    }
}