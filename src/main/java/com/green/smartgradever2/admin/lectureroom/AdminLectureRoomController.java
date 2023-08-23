package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.entity.LectureRoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lecture-room")
public class AdminLectureRoomController {

    private final AdminLectureRoomService SERVICE;

    @PostMapping
    public Long postLectureRoom(LectureRoomEntity entity) {
        return SERVICE.insLectureRoom(entity);
    }
}
