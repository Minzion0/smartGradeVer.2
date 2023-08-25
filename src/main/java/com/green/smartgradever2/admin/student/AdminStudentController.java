package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.student.model.*;
import com.green.smartgradever2.entity.StudentEntity;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminStudentController {

    private final AdminStudentService SERVICE;

    @PostMapping("/students")
    @Operation(summary = "학생등록")
    public ResponseEntity<?> studentEnrollment(@RequestBody AdminInsStudentParam param) throws Exception{
        AdminInsStudentVo vo = SERVICE.insStudent(param);

        return ResponseEntity.ok().body(vo);
    }

    @GetMapping("/students")
    @Operation(summary = "학생 검색", description = "학번이나 이름 둘중 하나로 찾기가능")
    public ResponseEntity<AdminStudentRes> searchStudent(@RequestParam(required = false) Integer studentNum, @RequestParam(required = false)String nm, @PageableDefault(sort="student_num", direction = Sort.Direction.DESC, size=10 )Pageable page
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


}
