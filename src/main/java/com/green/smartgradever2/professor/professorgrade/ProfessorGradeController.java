package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam Long iprofessor,
            @RequestParam Long ilecture,
            @RequestBody ProfessorGradeDto updatedGrade) {
        try {
            StudentGradeDTO updatedStudentGrade = service.updateStudentGrade(ilectureStudent, iprofessor, ilecture, updatedGrade);
            return ResponseEntity.ok(updatedStudentGrade);
        } catch (GradeExceedsMaxScoreException e) {
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping // 학생 성적 리스트
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
            "rating : 평점" )
    public ProfessorGradeStudentDto getProfessorStudents(@RequestParam Long iprofessor,@RequestParam Long isemester) {
        return service.getProGraStu(iprofessor,isemester);
    }



    @GetMapping("/list")
    @Operation(summary = "교수 강의를 듣는 학생 리스트",description ="ilectureStudent : 수강pk<br>"+
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
            "rating : 평점"  )
    public List<ProStudentLectureDto> getProStuList(@RequestParam Long iprofessor, @RequestParam Long isemester) {
        return service.getProList(iprofessor, isemester);
    }

    @PutMapping("/objection")
    @Operation(summary = "교수 학생 이의신청 수정" ,description = "ilecture : 강의PK<br>"+
            "ilectureStudent : 수강pk<br>"+"newObjection : 2적으면 이의신청완료<br> "
    +"<br>"+" 2적으면 강의 학생의 이의제기가 업데이트되었습니다. 메세지 출력")
    public ResponseEntity<String> updateObjection(
            @RequestParam Long ilecture,
            @RequestParam Long ilectureStudent,
            @RequestParam int newObjection) {
        try {
            service.updateObjection(ilecture, ilectureStudent, newObjection);
            return ResponseEntity.status(HttpStatus.OK).body("강의 학생의 이의제기가 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다.");
        }
    }

}
