package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.lecture_apply.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
@Tag(name = "교수 강의")
public class LectureApplyController {
    private  final LectureApplyService service;
    private final LectureApplyRepository lectureApplyRepository;

    @PostMapping("/lecture/apply")
    @Operation(summary = "강의 등록", description = "ilecture : 강의신청 pk<br>" +
            "ilecture_name : 강의pk<br>" + "ilecture_room : 강의실pk<br>" + "iprofessor : 교수pk<br>"
            + "isemester : 학기pk<br>" + "openingProcedures : 개강절차 0~5<br>" + "lectureStrDate : 개강시작 일자<br>"
            + "lectureEndDate : 개강종료 일자<br>" + "lectureStrTime : 수업시작시간<br>" + "lectureEndTime : 수업종료시간<br>" + "dayWeek : 강의요일0~6 일요일~토요일<br>"
            + "attendace : 출결 배점<br>" + "midtermExamination : 중간고사 배점<br>" + "finalExamination : 기말고사 배점<br>"
            + "lectureMaxPeople : 강의최대 인원 1~30<br>" + "gradeLimit : 신청할수있는 학년범위 1~4<br>" + "delYn : 삭제 여부<br>"
            + "<br>" + "기본 배점 출결(20),중간고사(40),기말고사(40)<br>")
    public LectureApplyRes postApply(@AuthenticationPrincipal MyUserDetails details, @RequestBody LectureAppllyInsParam param) throws Exception {

        return service.InsApply(details.getIuser(), param);
    }


    @GetMapping("/lecture/list")
    @Operation(summary = "신청중인 강의 리스트")
    private ResponseEntity<LectureSelAllRes> getLecture(@AuthenticationPrincipal MyUserDetails details
            ,@ParameterObject @PageableDefault(sort="ilecture", direction = Sort.Direction.DESC, size=10 ) Pageable page
            ,@ParameterObject @RequestParam(required = false) String LectureName
            ,@RequestParam(required = false) Integer openingProceudres
    ) {
         page = PageRequest.of(page.getPageNumber(), page.getPageSize(),Sort.by(Sort.Direction.DESC, "ilecture"));

        LectureSelAllRes lectureSelAllRes = service.getList(details.getIuser(),LectureName,page,openingProceudres);

        return ResponseEntity.ok(lectureSelAllRes);
    }

    @GetMapping("/lecture/room")
    @Operation(summary = "강의실 시간표")
    public ResponseEntity<LectureApplyScheduleRes> lectureRoomSchedule(@RequestParam Long ilectureRoom){

        LectureApplyScheduleRes lectureApplyScheduleRes = service.lectureRoomSchedule(ilectureRoom);
        return ResponseEntity.ok().body(lectureApplyScheduleRes);
    }
}