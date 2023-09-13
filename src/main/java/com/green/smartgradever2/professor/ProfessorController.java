package com.green.smartgradever2.professor;

import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.professor.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor")
@Tag(name = "교수")
public class ProfessorController {
    private final ProfessorService SERVICE;


    //    @GetMapping("/{iprofessor}")
//    @Operation(summary = "교수 프로필")
//    public ProfessorProfileDto getProfessorProfile(@PathVariable Long iprofessor) {
//        return SERVICE.getProfessorProfile(iprofessor);
//    }
    @GetMapping
    @Operation(summary = "교수프로필 디테일", description = "majorName : 전공이름<br>" + "name : 교수이름"
            + "gender : 성별<br>" + "pic : 프로필 사진<br>" + "birthDate : 생년월일<br>" + "phone : 폰번호<br>" + "email : 이메일<br>"
            + "address : 주소<br>" + "ilecture : 강의 pk<br>" + "lectureStrDate : 강의시작날짜<br>" +
            "lectureEndDate : 강의 종료날짜<br>" + "lectureStrTime : 강의 시작시간<br>" + "lectureEndTime : 강의종료시간<br>"
            + "lectureName: 강의명")
    public ProfessorSelRes getProfessorWithLectures(@AuthenticationPrincipal MyUserDetails details) {

        return SERVICE.getProfessorLectures(details.getIuser());
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "교수 프로필 수정", description = "address : 주소<br>" + "phone : 폰번호" + "eamil : 이메일<br>" +
            "pic : 프로필사진")
    public ResponseEntity<ProfessorUpRes> updateProfessorProfile(
            @RequestPart(required = false) MultipartFile pic,
            @AuthenticationPrincipal MyUserDetails details,
            @RequestPart ProfessorParam param) {
        ProfessorUpdDto dto = new ProfessorUpdDto();
        dto.setPhone(param.getPhone());
        dto.setAddress(param.getAddress());
        dto.setEmail(param.getEmail());


        try {
            // 기존 사진 삭제 처리
            SERVICE.processProfessorPicDeletion(details.getIuser());

            // 교수 정보 및 사진 업데이트 처리
            ProfessorUpRes response = SERVICE.upProfessor(pic, param, details.getIuser());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/lecture-list")
    @Operation(summary = "본인의 강의 조회", description = "ilecture : 강의pk<br>" +
            "lectureName : 강의명<br>" +
            "lectureRoomName : 강의실<br>" +
            "isemester : 학기 <br>" +
            "openingProceudres : 신청 절차 0반려,1신청,2신청완료모집,3개강,4종료<br>" +
            "gradeLimit : 학년 제한 <br>" +
            "lectureMaxPeople : 수강인원<br>" +
            "score : 강의 학점<br>" +
            "delYn : 삭제 여부 <br>")
    public ProfessorSelLectureRes getLecturePro(@AuthenticationPrincipal MyUserDetails details
            , @ParameterObject @PageableDefault(sort = "ilecture", direction = Sort.Direction.DESC, size = 10) Pageable page

            , @RequestParam(required = false,defaultValue = "0") int year
            ,@RequestParam(required = false) String lectureName
             ) {
        ProfessorSelLectureDto dto = new ProfessorSelLectureDto();
        dto.setIprofessor(details.getIuser());
        dto.setYear(year);
        dto.setLectureName(lectureName);

        return SERVICE.selProfessorLecture(dto, page);

    }


    @PutMapping("/change-password")
    @Operation(summary = "비밀번호 변경",
            description = "currentProfessorPassword : 현재 비밀번호 <br>" + "professorPassword : 바꿀 비밀번호")
    public ResponseEntity<?> updPassword(@AuthenticationPrincipal MyUserDetails details,
                                         @RequestBody ProfessorUpdPasswordParam param) throws Exception {
        ProfessorUpdPasswordDto dto = new ProfessorUpdPasswordDto();
        Long iuser = details.getIuser();
        String role = details.getRoles().get(0);
        dto.setIprofessor(iuser);
        dto.setRole(role);
        return ResponseEntity.ok().body(SERVICE.updPassword(dto, param));
    }


    //    @GetMapping("/sssss")
//    @Operation(summary = "학생 이의신청 리스트",description = "ilecture : 강의 PK<br>"+"objection : 이의신청 1 <br>"+
//            "grade : 알파벳 등급 <br>"+
//            "totalScore : 총점<br>"+
//            "studentNum : 학번<br>"+
//            "studentName : 학생이름")
//    public ResponseEntity<List<ProfessorStudentData>> getStudentsWithObjectionAndScores(
//            @RequestParam Long ilecture,
//            @RequestParam int objection,
//            @AuthenticationPrincipal MyUserDetails details) {
//        Long professorId = details.getIuser();
//
//        List<ProfessorStudentData> studentsWithObjectionAndScores = SERVICE.getStudentsWithObjectionAndScores(ilecture, objection,professorId);
//
//        return ResponseEntity.ok(studentsWithObjectionAndScores);
//    }
    @GetMapping("/schedule")
    @Operation(summary = "교수 본인 시간표")
    public List<ProfessorScheduleRes> professorScheduleList(@AuthenticationPrincipal MyUserDetails details) {
        List<ProfessorScheduleRes> professorScheduleRes = SERVICE.professorScheduleList(details.getIuser());
        return professorScheduleRes;
    }


    @GetMapping("/objection")
    @Operation(summary = "학생 이의신청 리스트", description = "ilecture: 강의 PK<br>"
            + "grade: 알파벳 등급 <br>" + "totalScore: 총점<br>" + "studentNum: 학번<br>"
            + "studentName: 학생이름")
    public ResponseEntity<List<ProfessorStudentData>> getStudentsWithObjectionAndScores(
            @RequestParam(required = false) Long ilecture,
            @AuthenticationPrincipal MyUserDetails details
            , @ParameterObject @PageableDefault(sort = "ilecture", direction = Sort.Direction.DESC, size = 10) Pageable page) {


        List<ProfessorStudentData> studentsWithObjectionAndScores = SERVICE.getStudentObjection(ilecture, details.getIuser(), page);

        return ResponseEntity.ok(studentsWithObjectionAndScores);
    }

}
