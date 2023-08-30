package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnSelDto;
import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnSelRES;
import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnUpParam;
import com.green.smartgradever2.professor.professorgrade.model.ProfessorGradeMngmnUpRes;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor/grade")
public class ProfessorGradeController {
    private final ProfessorGradeSevice service;



    @PutMapping
    @Operation(summary = "성적 입력",description = "attendace : 출결<br>"+"midtermExamination : 중간고사점수<br>"
            +"finalExamination <br>: 기말고사 점수"+"finishedYn : 수료여부 1 수료 0 수강중<br>"+"ilectureStudent : 학생pk<br>"+
            "ipofessor:교수pk<br>"+"point : 총점수 <br>"+"rating : 알파벳등급<br>"+"ilecture : 강의pk<br>"
            +"msg : 강사가 지정한 출결,중간고사,기말고사 배점이 넘으면 에러메세지 뜨면서 모두 null값으로 안들어감")
    public ProfessorGradeMngmnUpRes putProfessorGradeMngnm(@RequestParam Long iprofessor
            , @RequestParam Long ilecture
            , @RequestParam Long ilectureStudent
            , @RequestBody ProfessorGradeMngmnUpParam param) {


        return service.upMngnm(param, iprofessor, ilecture,ilectureStudent);
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
