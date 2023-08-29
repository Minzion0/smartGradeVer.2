package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grade-mngmn")
@RequiredArgsConstructor
@Tag(name = "관리자 통합 성적 관리")
public class GradeMngmnController {

    @Autowired
    private final GradeMngmnService SERVICE;

    @PostMapping
    @Operation(summary = "SEMESTER_SCORE INSERT")
    public GradeMngmnRes postGradeMngmn(@RequestBody GradeMngmnInsDto dto) {
        return SERVICE.postGradeMngmn(dto);
    }

    @PutMapping
    @Operation(summary = "SEMESTER_SCORE_PUT")
    public GradeMngmnUpdRes putGradeMngmn(@RequestParam Integer studentNum, @RequestParam int semester) {
        GradeMngmnUpdParam p = new GradeMngmnUpdParam();
        p.setSemester(semester);
        p.setStudentNum(studentNum);
        return SERVICE.updGradeMngmn(p);
    }

    @GetMapping
    @Operation(summary = " 학번으로 조회")
    public GradeMngmnFindRes getGradeMngmn(@PageableDefault(sort = "student_num", direction = Sort.Direction.DESC) Pageable pageable,
                                           @RequestParam(required = false, defaultValue = "0") int grade,
                                           @RequestParam Integer studentNum) {

        GradeMngmnDto dto = new GradeMngmnDto();
        dto.setStudentNum(studentNum);
        dto.setGrade(grade);
        dto.setSize(pageable.getPageSize());
        dto.setPage(pageable.getPageNumber());
        return SERVICE.selGradeMngmn(dto);
    }

    @GetMapping("/{studentNum}")
    @Operation(summary = "상세보기")
    public GradeMngmnDetailVo getGradeMngmnDetail(@PathVariable int studentNum) {
        GradeMngmnDetailSelDto dto = new GradeMngmnDetailSelDto();
        dto.setStudentNum(studentNum);
        return SERVICE.selStudentDetail(dto);
    }
}
