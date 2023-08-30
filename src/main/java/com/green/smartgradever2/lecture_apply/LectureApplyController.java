package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.lecture_apply.model.LectureApllySelRes;
import com.green.smartgradever2.lecture_apply.model.LectureAppllyInsDto;
import com.green.smartgradever2.lecture_apply.model.LectureAppllyInsParam;
import com.green.smartgradever2.lecture_apply.model.LectureAppllyRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "교수 강의")
public class LectureApplyController {
    private  final LectureApplyService service;

    @PostMapping
    @Operation(summary = "강의 등록", description = "ilecture : 강의신청 pk<br>" +
            "ilecture_name : 강의pk<br>" + "ilecture_room : 강의실pk<br>" + "iprofessor : 교수pk<br>"
            + "isemester : 학기pk<br>" + "openingProcedures : 개강절차 0~5<br>" + "lectureStrDate : 개강시작 일자<br>"
            + "lectureEndDate : 개강종료 일자<br>" + "lectureStrTime : 수업시작시간<br>" + "lectureEndTime : 수업종료시간<br>" + "dayWeek : 강의요일0~6 일요일~토요일<br>"
            + "attendace : 출결 배점<br>" + "midtermExamination : 중간고사 배점<br>" + "finalExamination : 기말고사 배점<br>"
            + "lectureMaxPeople : 강의최대 인원 1~30<br>" + "gradeLimit : 신청할수있는 학년범위 1~4<br>" + "delYn : 삭제 여부<br>"
            + "<br>" + "기본 배점 출결(20),중간고사(40),기말고사(40)<br>")
    public LectureAppllyRes postApply(@RequestParam Long iprofessor, @RequestBody LectureAppllyInsParam param) {
        LectureAppllyInsDto dto = new LectureAppllyInsDto();
        dto.setIlectureName(param.getIlectureName());
        dto.setIlectureRoom(param.getIlectureRoom());
        dto.setIprofessor(iprofessor);

        dto.setLectureEndTime(param.getLectureEndTime());
        dto.setLectureStrTime(param.getLectureStrTime());
        dto.setDayWeek(param.getDayWeek());
        dto.setAttendance(param.getAttendance());
        dto.setMidtermExamination(param.getMidtermExamination());
        dto.setFinalExamination(param.getFinalExamination());
        dto.setLectureMaxPeople(param.getLectureMaxPeople());

        return service.InsApplly(dto);
    }


    @GetMapping
    @Operation(summary = "신청중인 강의 리스트 뽑기", description = "iprofessor : 교수 pk<br>" + "ilecture : 강의신청pk<br>" + "ilectureName : 강의 이름pk<br>"
            + "ilectureRoom : 강의실pk<br> " + "isemester : 학기pk 1학기 = 20 2학기 = 21<br>" + "dayWeek : 강의 시작 요일<br>" + "lectureStrDate : 강의 시작일자<br>" + "lectureEndDate : 강의 종료일자<br>"
            + "lectureStrTime : 강의 시작시간<br>" + "lectureEndTime : 강의 종료시간<br>" + "lectureMaxPeople : 수강인원 최대 30명<br>" + "gradeLimit : 신청할수있는 학년범위 1~4<br>"
            + "openingProcedures : 신청절차  0 반려 1개강신청 2개강인원모집중 3개강 4수강종료 1만나오게함<br>")
    public LectureApllySelRes getLectureApplly
            (@RequestParam(defaultValue = "1") int page, @RequestParam Long iprofessor) {


        return service.selLectureApplly(page, iprofessor);

    }
}