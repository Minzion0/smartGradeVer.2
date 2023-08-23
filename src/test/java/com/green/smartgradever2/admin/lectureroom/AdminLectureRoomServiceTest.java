package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.entity.LectureRoomEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
}