package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/grade")
@Tag(name = "교수 강의 성적")
public class ProfessorGradeController {
    private final ProfessorGradeSevice service;



    @PutMapping
    @Operation(summary = "성적입력",description = "ilectureStudent : 수강pk<br>"+"iprofessor : 교수PK<br>"+"ilecture : 강의PK<br>"
           +"attendance : 출석점수<br>"+
            "midtermExamination : 중간고사 점수 <br>"+
            "finalExamination : 기말고사 점수 <br>"+
            "totalScore : 총점수 <br>"+
            "grade : 알파벳등급 <br>"+
            "rating : 평점")
    public ResponseEntity<StudentGradeDTO> updateStudentGrade(
            @RequestParam Long ilectureStudent,
            @AuthenticationPrincipal MyUserDetails details,
            @RequestParam Long ilecture,
            @RequestBody ProfessorGradeDto updatedGrade) {
        try {
            StudentGradeDTO updatedStudentGrade = service.updateStudentGrade(ilectureStudent, details.getIuser(), ilecture, updatedGrade);
            return ResponseEntity.ok(updatedStudentGrade);
        } catch (GradeExceedsMaxScoreException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping // 학생 성적 리스트
//
//    public ProfessorGradeStudentRes getProfessorStudents(@AuthenticationPrincipal MyUserDetails details
//            , @RequestParam(required = false) Long ilecture
//    , @ParameterObject @PageableDefault(sort="ilecture", direction = Sort.Direction.DESC, size=10 ) Pageable page) {
//        return service.getProGraStu(details.getIuser(), ilecture ,page);
//    }

    @GetMapping
    @Operation(summary = "교수 강의 학생성적리스트",description = "ilectureStudent : 수강pk<br>"+
            "ilecture : 강의PK<br>"+
            "sudentNum : 학생학번<br>"+
            "lectureEndDate : 강의 종료 일자<br>"+
            "attendance : 출석점수<br>"+
            "midtermExamination : 중간고사 점수 <br>"+
            "finalExamination : 기말고사 점수 <br>"+
            "dayWeek : 요일<br>"+
            "lectureStrTime : 강의시작시간<br>"+
            "lectureEndTime : 강의종료시간<br>"+
            "totalScore : 총점수 <br>"+
            "grade : 알파벳등급 <br>"+
            "rating : 평점"
           +"finishedYn : 0,1 수료여부")
    public Map<String, Object> getProfessorGrades(
            @AuthenticationPrincipal MyUserDetails details,
            @ParameterObject @PageableDefault(sort = "ilecture", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(required = false) Long ilecture
    ) {

        return service.getProfessorGradeList(details.getIuser(), ilecture, pageable);
    }

//    @GetMapping("/list")
//    @Operation(summary = "교수 강의를 듣는 학생 리스트",description ="ilectureStudent : 수강pk<br>"+
//            "ilecture : 강의PK<br>"+
//            "sudentNum : 학생학번<br>"+
//            "lectureEndDate : 강의 종료 일자<br>"+
//            "attendance : 출석점수<br>"+
//            "midtermExamination : 중간고사 점수 <br>"+
//            "finalExamination : 기말고사 점수 <br>"+
//            "dayWeek : 요일<br>"+
//            "lectureStrTime : 강의시작시간<br>"+
//            "lectureEndTime : 강의종료시간<br>"+
//            "totalScore : 총점수 <br>"+
//            "grade : 알파벳등급 <br>"+
//            "rating : 평점"  )
//    public ProfessorStuLectureRes getProStuList(@AuthenticationPrincipal MyUserDetails details,
//                                                          @RequestParam(required = false) Long ilecture,
//                                                          @ParameterObject @PageableDefault(sort="ilecture", direction = Sort.Direction.DESC, size=10 ) Pageable page) {
//
//        return service.getProList(details.getIuser(),ilecture ,page);
//    }


    @PutMapping("/objection")
    @Operation(summary = "교수 학생 이의신청 수정" ,description = "ilecture : 강의PK<br>"+
            "ilectureStudent : 수강pk<br>"+"newObjection : 2적으면 이의신청완료<br> "
    +"<br>"+" 2적으면 강의 학생의 이의제기가 업데이트되었습니다. 메세지 출력")
    public ResponseEntity<String> updateObjection(
            @RequestParam Long ilecture,
            @RequestParam Long ilectureStudent,
            @RequestParam int newObjection
            ,@AuthenticationPrincipal MyUserDetails details) {
        try {
            service.updateObjection(ilecture, ilectureStudent, newObjection,details.getIuser());
            return ResponseEntity.status(HttpStatus.OK).body("강의 학생의 이의제기가 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        }
    }

//    @GetMapping("/list")
//    @Operation(summary = "교수 강의를 듣는 학생 리스트",description ="ilectureStudent : 수강pk<br>"+
//            "ilecture : 강의PK<br>"+
//            "sudentNum : 학생학번<br>"+
//            "lectureEndDate : 강의 종료 일자<br>"+
//            "attendance : 출석점수<br>"+
//            "midtermExamination : 중간고사 점수 <br>"+
//            "finalExamination : 기말고사 점수 <br>"+
//            "dayWeek : 요일<br>"+
//            "lectureStrTime : 강의시작시간<br>"+
//            "lectureEndTime : 강의종료시간<br>"+
//            "totalScore : 총점수 <br>"+
//            "grade : 알파벳등급 <br>"+
//            "rating : 평점"  )
//    public ProfessorListStudentRes getProStuList(@AuthenticationPrincipal MyUserDetails details,
//                                                @RequestParam(required = false) Long ilecture,
//                                                @ParameterObject @PageableDefault(sort="ilecture", direction = Sort.Direction.DESC, size=10 ) Pageable page,
//                                                @RequestParam(required = false,defaultValue = "0") int year,
//                                                 @RequestParam(required = false) Long studentNum,
//                                                 @RequestParam(required = false) String nm
//                                   ) {
//
//        return service.getProList(details.getIuser(),ilecture,year,studentNum,nm,page);
//    }
    @GetMapping("/lecture-student-list")
    @Operation(summary = "내 강의를 듣고 있는 학생 목록", description = "iprofessor : 교수pk<br>"+ "studentNum : 학생학번<br>"
            + "nm : 학생이름<br>"
            + "majorName : 전공<br>"
            +"attendance : 출결점수<br>"
            +"midtermExamination : 중간고사 점수 <br>"
            +"finalExamination 기말고사 점수 <br>"
            +"point : 평점 <br>"
            +"grade : 알파벳 등급<br>")
    public ProfessorGradeMngmnSelRES selStudentScore(@AuthenticationPrincipal MyUserDetails details,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam Integer ilecture,
                                                     @RequestParam(defaultValue = "0") int studentNum,
                                                     @RequestParam(required = false) String nm
                                                      ) {
        ProfessorGradeMngmnSelDto dto = new ProfessorGradeMngmnSelDto();
        dto.setIprofessor(details.getIuser());
        dto.setPage(page);
        dto.setIlecture(ilecture);
        dto.setStudentNum(studentNum);
        dto.setNm(nm);

        return service.selStudentScore(dto);
    }
}
