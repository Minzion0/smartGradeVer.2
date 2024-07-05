package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.lecture_apply.model.LectureAppllyInsParam;
import com.green.smartgradever2.lecture_apply.model.LectureApplyRes;
import com.green.smartgradever2.lecture_apply.model.LectureApplyScheduleRes;
import com.green.smartgradever2.lecture_apply.model.LectureSelAllRes;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
@Tag(name = "교수 강의")
public class LectureApplyController {

    // LectureApplyService를 주입받아 사용
    private final LectureApplyService service;
    // LectureApplyRepository를 주입받아 사용
    private final LectureApplyRepository lectureApplyRepository;

    /**
     * 강의 등록 엔드포인트
     *
     * @param details 인증된 사용자 정보
     * @param param 강의 신청 파라미터
     * @return 강의 신청 결과
     * @throws Exception 예외 발생 시
     */
    @PostMapping("/lecture/apply")
    @Operation(summary = "강의 등록", description = "ilecture : 강의신청 pk<br>" +
        "ilecture_name : 강의pk<br>" + "ilecture_room : 강의실pk<br>" + "iprofessor : 교수pk<br>"
        + "isemester : 학기pk<br>" + "openingProcedures : 개강절차 0~5<br>" + "lectureStrDate : 개강시작 일자<br>"
        + "lectureEndDate : 개강종료 일자<br>" + "lectureStrTime : 수업시작시간<br>" + "lectureEndTime : 수업종료시간<br>" + "dayWeek : 강의요일0~6 일요일~토요일<br>"
        + "attendace : 출결 배점<br>" + "midtermExamination : 중간고사 배점<br>" + "finalExamination : 기말고사 배점<br>"
        + "lectureMaxPeople : 강의최대 인원 1~30<br>" + "gradeLimit : 신청할수있는 학년범위 1~4<br>" + "delYn : 삭제 여부<br>"
        + "<br>" + "기본 배점 출결(20),중간고사(40),기말고사(40)<br>")
    public LectureApplyRes postApply(@AuthenticationPrincipal MyUserDetails details, @RequestBody LectureAppllyInsParam param) throws Exception {
        // 강의 신청 서비스 호출
        return service.InsApply(details.getIuser(), param);
    }

    /**
     * 신청중인 강의 리스트 조회 엔드포인트
     *
     * @param details 인증된 사용자 정보
     * @param page 페이징 정보
     * @param LectureName 강의 이름 (선택적)
     * @param openingProceudres 개강 절차 상태 (선택적)
     * @return 신청중인 강의 리스트
     */
    @GetMapping("/lecture/list")
    @Operation(summary = "신청중인 강의 리스트")
    private ResponseEntity<LectureSelAllRes> getLecture(@AuthenticationPrincipal MyUserDetails details
        , @ParameterObject @PageableDefault(sort = "ilecture", direction = Sort.Direction.DESC, size = 10) Pageable page
        , @ParameterObject @RequestParam(required = false) String LectureName
        , @RequestParam(required = false) Integer openingProceudres) {

        // 페이지 요청 객체 생성
        page = PageRequest.of(page.getPageNumber(), page.getPageSize(), Sort.by(Sort.Direction.DESC, "ilecture"));

        // 서비스 호출하여 강의 리스트 조회
        LectureSelAllRes lectureSelAllRes = service.getList(details.getIuser(), LectureName, page, openingProceudres);

        // 결과 반환
        return ResponseEntity.ok(lectureSelAllRes);
    }

    /**
     * 강의실 시간표 조회 엔드포인트
     *
     * @param ilectureRoom 강의실 ID
     * @return 강의실 시간표
     */
    @GetMapping("/lecture/room")
    @Operation(summary = "강의실 시간표")
    public ResponseEntity<LectureApplyScheduleRes> lectureRoomSchedule(@RequestParam Long ilectureRoom) {
        // 서비스 호출하여 강의실 시간표 조회
        LectureApplyScheduleRes lectureApplyScheduleRes = service.lectureRoomSchedule(ilectureRoom);

        // 결과 반환
        return ResponseEntity.ok().body(lectureApplyScheduleRes);
    }
}