package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.*;
import com.green.smartgradever2.config.entity.LectureRoomEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "관리자 강의실 관리")
public class AdminLectureRoomController {

    private final AdminLectureRoomService SERVICE;

    @PostMapping("/admin/lectureroom")
    @Operation(summary = "강의실 추가")
    public Long postLectureRoom(@RequestBody AdminLectureRoomInsDto dto) {
        return SERVICE.insLectureRoom(dto);
    }

    @GetMapping("/admin/lectureroom")
    @Operation(summary = "강의실 리스트 보기")
    public AdminLectureRoomFindRes getLectureRoom(@ParameterObject @PageableDefault(sort="ilectureRoom", direction = Sort.Direction.DESC) Pageable pageable,
                                                       @RequestParam (required = false) String buildingName,
                                                       @RequestParam (required = false) String lectureRoomName) {
        AdminLectureRoomDto dto = new AdminLectureRoomDto();
        dto.setLectureRoomName(lectureRoomName);
        dto.setBuildingName(buildingName);
        dto.setSize(pageable.getPageSize());
        dto.setPage(pageable.getPageNumber());
        return SERVICE.selLectureRoom(dto, pageable);
    }

    @DeleteMapping("/admin/lectureroom")
    @Operation(summary = "강의실 삭제 (delYn 0 1 변경)")
    public AdminLectureRoomListVo patchLectureRoom(@RequestParam Long ilectureRoom) {
        LectureRoomEntity entity = new LectureRoomEntity();
        entity.setIlectureRoom(ilectureRoom);

        return SERVICE.delLectureRoom(entity);
    }

    @GetMapping("/lectureroom/list")
    @Operation(summary = "강의실 리스트 페이징x")
    public ResponseEntity<List<LectureRoomVo>> getLectureRoomList(){
        List<LectureRoomVo> lectureRoomList = SERVICE.getLectureRoomList();
        return ResponseEntity.ok().body(lectureRoomList);
    }





}
