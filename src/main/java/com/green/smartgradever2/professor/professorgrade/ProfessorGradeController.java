package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/grade")
public class ProfessorGradeController {
    private final ProfessorGradeSevice service;



    @PutMapping
    @Operation(summary = "성적입력")
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
    @Operation(summary = "교수 강의 학생성적리스트")
    public ProfessorGradeStudentDto getProfessorStudents(@RequestParam Long iprofessor,@RequestParam Long isemester) {
        return service.getProGraStu(iprofessor,isemester);
    }



    @GetMapping("/list")
    @Operation(summary = "교수 강의를 듣는 학생 리스트")
    public List<ProStudentLectureDto> getProStuList(@RequestParam Long iprofessor, @RequestParam Long isemester) {
        return service.getProList(iprofessor, isemester);
    }

}
