package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import com.green.smartgradever2.student.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "학생")
public class StudentController {
    private final StudentService SERVICE;

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "학생 프로필 수정", description = "studentNum : 학번<br>" +
            "address : 주소<br>" +
            "phone : 폰번호<br>" +
            "email : 이메일<br>" +
            "pic : 학생프로필사진")
    public ResponseEntity<StudentUpRes> upStudentProfile(@RequestPart(required = false) MultipartFile pic
            , @RequestPart StudentParam param, @AuthenticationPrincipal MyUserDetails details) {
        StudentUpdDto dto = new StudentUpdDto();
        dto.setPhone(param.getPhone());
        dto.setAddress(param.getAddress());
        dto.setEmail(param.getEmail());

        try {
            //기존 사진 삭제
            SERVICE.studentDelPic(details.getIuser());
            //학생 정보 및 사진 업데이트 처리
            StudentUpRes upRes = SERVICE.upStudent(pic, param, details.getIuser());
            return ResponseEntity.ok(upRes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/lecture")
    @Operation(summary = "학생 수강 신청", description = "ilecture : 강의 PK<br>" + "studentNum : 학번<br>"
            + "success : 신청성공하면 true<br>" +
            "message : 수강신청완료메세지 <br> " +
            "ilectureStudent : 학생 수강pk<br>" +
            "ilecture : 교수강의 pk<br>" +
            "lectureName : 강의명<br>" +
            "attendance : 출석점수 <br>" +
            "midtermExamination : 중간고사점수<br>" +
            "finalExamination : 기말고사 점수<br>" +
            "totalScore : 총점수<br>" +
            "finishedAt : 수강완료날짜<br>" +
            "correctionAt : 이의신청기간<br>" +
            "finishedYn : 수강완료 표시 0,1<br>" +
            "delYn : 삭제 여부<br>" +
            "dayWeek : 요일<br>" +
            "lectureStrTime : 강의시작시간<br>" +
            "lectureEndTime : 강의종료시간<br>" +
            "objection : 이의신청 기본0 신청하면 1")
    public ResponseEntity<StudentRegisterRes> registerStudentLecture(@RequestBody StudentLectureReg request, @AuthenticationPrincipal MyUserDetails details) {
        StudentRegisterRes response = SERVICE.registerLectureForStudent(request.getIlecture(), details.getIuser());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/detail")
    @Operation(summary = "학생 프로필 디테일", description = "studentNum : 학번<br>" +
            "majorName : 전공<br>" +
            "name : 이름 <br>" +
            "grade : 학년<br>" +
            "gender : 성별<br>" +
            "address : 주소" +
            "phone : 폰번호<br>" +
            "birthDate : 생녕월일<br>" +
            "email : 이메일<br>" +
            "pic : 프로필사진<br>" +
            "finishedYn : 재학여부 <br>" +
            "score : 현재 이수학점" + "<br>" + "<br>"
            + "ilecture : 강의PK <br>" +
            "lectureName : 강의명<br>" +
            "lectureStrDate : 강의 시작날짜<br>" +
            "lectureEndDate : 강의 종료날짜<br>" +
            "lectureStrTime : 강의 시작시간<br>" +
            "lectureEndTime : 강의 종료시간<br>" +
            "score : 해당강의 학점")
    public StudentFileSelRes getStudentProfileDetail(@AuthenticationPrincipal MyUserDetails details) {
        return SERVICE.getStudentProfileWithLectures(details.getIuser());

    }

    @GetMapping
    @Operation(summary = "학생 강의별 성적 조회", description = "studentNum : 학번<br>" + "ilectureStudent : 학생수강PK<br>" + "ilecture : 강의pk <br>" + "isemester : 학기<br>" +
            "year : 년도" + "professorName : 담당교수이름 <br>" + "lectureName : 강의 명" + "dayWeek : 요일<br>" + "lectureStrTime : 강의 시작시간<br>" + "lectureEndTime : 강의 종료시간<br>" +
            "score : 해당강의 학점<br>" + "finishedYn : 수료여부 0<- 수강중 1<-수강완료<br>" + "objection : 이의신청 기본0 신청시1<br>" + "attendance : 출석점수 <br>" + "midtermExamination : 중간고사점수<br>" +
            "finalExamination : 기말고사 점수<br>" + "totalScore : 총점수<br>" + "grade : 평점<br>" + "rating : 알파벳 등급<br>" + "studentGrade : 학년")
    public ResponseEntity<List<StudentSelVo>> getStudentGrades(@AuthenticationPrincipal MyUserDetails details,
                                                               @ParameterObject @PageableDefault(sort = "ilecture", direction = Sort.Direction.DESC, size = 10) Pageable page) {
        StudentEntity studentEntity = SERVICE.getStudentById(details.getIuser()); // 학생 정보 가져오기
        if (studentEntity == null) {
            return ResponseEntity.notFound().build(); // 학생이 존재하지 않으면 404 반환
        }

        List<StudentSelVo> studentGrades = SERVICE.getStudentLectureGrades(studentEntity, page); // 성적 정보 가져오기
        return ResponseEntity.ok(studentGrades); // 성적 정보 리스트 반환
    }


    @GetMapping("/score")
    @Operation(summary = "학점 조회", description = "studentNum : 학번<br>" +
            "majorName : 전공<br>" +
            "selfStudyCredit : 이수학점<br>" +
            "remainingPoints : 남은학점<br>" +
            "graduationScore : 전공학점<br>")
    public StudentInfoDto getStudentInfo(@AuthenticationPrincipal MyUserDetails details) {
        return SERVICE.getStudentInfo(details.getIuser());
    }

    @PutMapping("/change-password")
    @Operation(summary = "비밀번호 변경",
            description = "studentPassword : 바꿀 비밀번호 <br>" + "currentStudentPassword : 현재 비밀번호")
    public ResponseEntity<?> updPassword(@AuthenticationPrincipal MyUserDetails details,
                                         @RequestBody StudentPasswordParam param) throws Exception {
        StudentUpdPasswordDto dto = new StudentUpdPasswordDto();
        dto.setRole(details.getRoles().get(0));
        dto.setIstudent(details.getIuser());

        return ResponseEntity.ok().body(SERVICE.updPassword(param, dto));
    }


    @PutMapping("/objection")
    @Operation(summary = "학생 이의신청", description = "StudentNum : 학생pk<br>" + "ilectureStudent : 학생이 들은 강의pk<br>"
            + "objection : 1로 보내면 이의 신청이 됨")
    public ResponseEntity<String> updateObjection(
            @AuthenticationPrincipal MyUserDetails details,
            @RequestParam Long ilectureStudent,
            @RequestBody StudentObjectionDto objectionDto) {
        try {
            SERVICE.updateObjection(details.getIuser(), ilectureStudent, objectionDto);
            return ResponseEntity.status(HttpStatus.OK).body("학생의 이의제의가 신청되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이의신청기간이 지났습니다.");
        }
    }

    @GetMapping("/lecture-list")
    @Operation(summary = "학생 강의조회", description = "year : 년도<br>" +
            "isemester : 학기<br>" + "lectureName : 강의명<br>" + "ProfessorName : 교수이름<br>" + "score : 학점<br>"
            + "lectureStrTime : 강의시작 시간<br>" + "lectureEndTime : 강의종료 시간<br>" +
            "finishedYn : 0수강중 1 수료<br>" + "openingProcedures : 강의 절차 1~4<br>")
    public StudentHistoryRes getStudentHistory(@AuthenticationPrincipal MyUserDetails details
            , @ParameterObject @PageableDefault(sort = "finishedYn", direction = Sort.Direction.DESC, size = 10) Pageable page

            , @RequestParam(required = false, defaultValue = "0") int year
            , @RequestParam(required = false) String lectureName
            , @RequestParam(required = false) String nm) {
        StudentHistoryOpenDto dto = new StudentHistoryOpenDto();
        dto.setStudentNum(details.getIuser());

        dto.setYear(year);
        dto.setNm(nm);
        dto.setLectureName(lectureName);
        return SERVICE.studentHistoryRes(dto, page);
    }


    @GetMapping("/lecturelist")
    @Operation(summary = "학생 수강신청 조회")
    public StudentListLectrueRes getAllProfessorsLecturesWithFilters(
            @AuthenticationPrincipal MyUserDetails details,
            @ParameterObject @PageableDefault(sort = "gradeLimit", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(required = false) String lectureName
    ) {
        StudentListLectureDto dto = new StudentListLectureDto();
        dto.setStudentNum(details.getIuser());
        dto.setOpeningProcedures(2);
        dto.setLectureName(lectureName);
        return SERVICE.getAllProfessorsLecturesWithFilters(dto, pageable);
    }

    @GetMapping("/grade-file")
    @Operation(summary = "학생 성적 출력")
    public void greenUniversityStudentFile(@AuthenticationPrincipal MyUserDetails details, HttpServletResponse request) throws IOException {
        log.info(details.getUid());
        SERVICE.studentGradePrint(details.getIuser(), request);
    }

    @GetMapping("/schedule")
    @Operation(summary = "학생 시간표")
    public List<StudentScheduleRes> studentSchedule(@AuthenticationPrincipal MyUserDetails details) {
        return SERVICE.studentSchedule(details.getIuser());
    }


    @DeleteMapping("/lecture")
    @Operation(summary = "강의 수강 신청 철회")
    public int studentDeleteLecture(@AuthenticationPrincipal MyUserDetails details, @RequestParam Long ilecture) {
        int result = SERVICE.lectureStudentDel(details.getIuser(), ilecture);

        return result;
    }

}
