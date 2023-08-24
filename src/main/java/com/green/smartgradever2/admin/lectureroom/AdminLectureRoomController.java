package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLecturRoomFindRes;
import com.green.smartgradever2.entity.LectureRoomEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectureroom")
@Tag(name = "관리자 강의실 관리")
public class AdminLectureRoomController {

    private final AdminLectureRoomService SERVICE;

    @PostMapping
    @Operation(summary = "강의실 추가")
    public Long postLectureRoom(LectureRoomEntity entity) {
        return SERVICE.insLectureRoom(entity);
    }

    @GetMapping
    @Operation(summary = "강의실 리스트 보기")
    public AdminLecturRoomFindRes getLectureRoom(LectureRoomEntity entity,
                                                 @PageableDefault(sort = "ilectureRoom", direction = Sort.Direction.ASC, size = 20) Pageable pageable) {
        return SERVICE.selLectureRoom(entity,pageable);
    }

    @DeleteMapping
    @Operation(summary = "강의실 삭제 (delYn 0 1 변경)")
    public Long delLectureRoom(LectureRoomEntity entity) {
        return SERVICE.delLectureRoom(entity);
    }
}
