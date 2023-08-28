package com.green.smartgradever2.student;

import com.green.smartgradever2.student.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


}
