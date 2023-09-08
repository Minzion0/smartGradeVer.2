package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.student.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "관리자 학생 관리")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminStudentController {

    private final AdminStudentService SERVICE;


    @PostMapping("/students")
    @Operation(summary = "학생 다중 등록")
    public ResponseEntity<?> studentEnrollmentTest(@RequestBody List<AdminInsStudentParam> param) throws Exception{
        List<AdminInsStudentVo> vo = SERVICE.insStudent(param);

        return ResponseEntity.ok().body(vo);
    }

    @GetMapping("/students")
    @Operation(summary = "학생 검색", description = "학번이나 이름 둘중 하나로 찾기가능")
    public ResponseEntity<AdminStudentRes> searchStudent(@RequestParam(required = false) Integer studentNum, @RequestParam(required = false)String nm,@ParameterObject @PageableDefault(sort="createdAt", direction = Sort.Direction.DESC, size=10 )Pageable page
            , @RequestParam (defaultValue = "0") int grade, @RequestParam (defaultValue = "0",required = false) int finishedYn
            , @RequestParam (required = false,defaultValue = "0")Long imajor){

        AdminStudentFindParam param = AdminStudentFindParam.builder().imajor(imajor)
                                                                        .studentNum(studentNum)
                                                                        .finishedYn(finishedYn)
                                                                        .nm(nm)
                                                                        .grade(grade)
                                                                        .build();

        return ResponseEntity.ok().body(SERVICE.findStudents(param,page));
    }

    @GetMapping("/students/{studentNum}")
    @Operation(summary = "학생 디테일")
    public ResponseEntity<AdminStudentDetailRes> studentDet(@PathVariable int studentNum){
        return ResponseEntity.ok().body(SERVICE.studentDet(studentNum));
    }


    @PatchMapping("/students/{studentNum}")
    @Operation(summary = "학생 정보 수정")
    public ResponseEntity<AdminInsStudentVo> patchStudent(@PathVariable Long studentNum, @RequestBody AdminStudentPatchParam param) throws Exception {
        AdminInsStudentVo vo = SERVICE.patchStudent(studentNum, param);

        return ResponseEntity.ok().body(vo);
    }


    @GetMapping("/student-file")
    @Operation(summary = "대학 학생 구성원들 정보")
    public void greenUniversityMember(HttpServletResponse request) throws IOException {
        SERVICE.studentListFile(request);
    }

}
