package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/lecture-student-list")
    @Operation(summary = "내 강의를 듣고 있는 학생 목록", description = "iprofessor : 교수pk<br>"+ "studentNum : 학생학번<br>"
            + "nm : 학생이름<br>"
            + "majorName : 전공<br>"
            +"attendance : 출결점수<br>"
            +"midtermExamination : 중간고사 점수 <br>"
            +"finalExamination 기말고사 점수 <br>"
            +"point : 평점 <br>"
            +"grade : 알파벳 등급<br>")
    public ProfessorGradeMngmnSelRES selStudentScore(@RequestParam Long iprofessor,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "0") int ilecture,
                                                     @RequestParam(defaultValue = "0") int studentNum) {
        ProfessorGradeMngmnSelDto dto = new ProfessorGradeMngmnSelDto();
        dto.setIprofessor(iprofessor);
        dto.setPage(page);
        dto.setIlecture(ilecture);
        dto.setStudentNum(studentNum);
        return service.selStudentScore(dto);
    }

}
