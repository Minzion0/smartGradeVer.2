package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import com.green.smartgradever2.student.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@Tag(name = "학생")
public class StudentController {
    private final StudentService SERVICE;

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "학생 프로필 수정")
    public ResponseEntity<StudentUpRes> upStudentProfile(@RequestPart(required = false) MultipartFile pic
            , @RequestPart StudentUpdDto dto) {
        StudentParam param = new StudentParam();
        param.setPhone(dto.getPhone());
        param.setEmail(dto.getEmail());
        param.setStudentNum(dto.getStudentNum());
        param.setAddress(dto.getAddress());

        try {
            //기존 사진 삭제
            SERVICE.studentDelPic(dto.getStudentNum());
            //학생 정보 및 사진 업데이트 처리
            StudentUpRes upRes = SERVICE.upStudent(pic, param);
            return ResponseEntity.ok(upRes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping
    @Operation(summary = "학생 수강 신청")
    public ResponseEntity<StudentRegisterRes> registerStudentLecture(@RequestBody StudentLectureReg request) {
        StudentRegisterRes response = SERVICE.registerLectureForStudent(request.getIlecture(), request.getStudentNum());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{studentNum}")
    @Operation(summary = "학생 프로필 디테일")
    public StudentFileSelRes getStudentProfileDetail(@PathVariable Long studentNum) {
    return    SERVICE.getStudentProfileWithLectures(studentNum);

    }

    @GetMapping
    @Operation(summary = "학생 강의별 성적 조회")
    public ResponseEntity<List<StudentSelVo>> getStudentGrades(@RequestParam Long studentNum) {
        StudentEntity studentEntity = SERVICE.getStudentById(studentNum); // 학생 정보 가져오기
        if (studentEntity == null) {
            return ResponseEntity.notFound().build(); // 학생이 존재하지 않으면 404 반환
        }

        List<StudentSelVo> studentGrades = SERVICE.getStudentLectureGrades(studentEntity); // 성적 정보 가져오기
        return ResponseEntity.ok(studentGrades); // 성적 정보 리스트 반환
    }


    @GetMapping("/{studentNum}/score")
    @Operation(summary = "학점 조회")
    public StudentInfoDto getStudentInfo(@PathVariable Long studentNum) {
        return SERVICE.getStudentInfo(studentNum);
    }

    @PutMapping("/changPassword")
    @Operation(summary = "비밀번호 변경",
            description = "studentPassword : 바꿀 비밀번호 <br>" + "currentStudentPassword : 현재 비밀번호")
    public ResponseEntity<?> updPassword(@AuthenticationPrincipal MyUserDetails details,
                                         @RequestBody StudentPasswordParam param) throws Exception {
        StudentUpdPasswordDto dto = new StudentUpdPasswordDto();
        dto.setRole(details.getRoles().get(0));
        dto.setIstudent(details.getIuser());

        return ResponseEntity.ok().body(SERVICE.updPassword(param, dto));
    }
}
