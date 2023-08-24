package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.student.model.AdminInsStudentParam;
import com.green.smartgradever2.admin.student.model.AdminInsStudentVo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminStudentController {

    private final AdminStudentService SERVICE;

    @PostMapping("/students")
    @Operation(summary = "학생등록")
    public ResponseEntity<?> studentEnrollment(@RequestBody AdminInsStudentParam param)/** throws AdminException **/{
        AdminInsStudentVo vo = SERVICE.insStudent(param);
        return ResponseEntity.ok().body(vo);
    }
//
//    @GetMapping("/students")
//    @Operation(summary = "학생 검색", description = "학번이나 이름 둘중 하나로 찾기가능")
//    public AdminStudentRes searchStudent(@RequestParam(required = false) String studentNum, @RequestParam(required = false)String nm, @RequestParam (defaultValue = "1") int page
//            ,@RequestParam (defaultValue = "0") int grade,@RequestParam (defaultValue = "0",required = false) int finishedYn
//            ,@RequestParam (required = false,defaultValue = "0")Long imajor){
//
//        AdminFindStudentDto dto = new AdminFindStudentDto();
//        dto.setGrade(grade);
//        dto.setNm(nm);
//        dto.setImajor(imajor);
//        dto.setFinishedYn(finishedYn);
//        dto.setStudentNum(studentNum);
//        return SERVICE.findStudents(dto,page);
//    }
//
//    @GetMapping("/students/{istudent}")
//    @Operation(summary = "학생 디테일")
//    public AdminStudentDetalRes studentDet(@PathVariable Long istudent){
//        return SERVICE.studentDet(istudent);
//    }
//

}
