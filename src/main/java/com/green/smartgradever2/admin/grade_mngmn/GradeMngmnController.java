package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/grade-mngmn")
@RequiredArgsConstructor
@Tag(name = "관리자 통합 성적 관리")
public class GradeMngmnController {

    @Autowired
    private final GradeMngmnService SERVICE;

//    @PostMapping
//    @Operation(summary = "SEMESTER_SCORE INSERT")
//    public GradeMngmnRes postGradeMngmn(@RequestBody GradeMngmnInsDto dto) {
//        return SERVICE.postGradeMngmn(dto);
//    }
////
//    @PutMapping
//    @Operation(summary = "SEMESTER_SCORE_PUT")
//    public GradeMngmnUpdRes putGradeMngmn(@RequestParam Integer studentNum, @RequestParam Long isemester) {
//        GradeMngmnUpdParam p = new GradeMngmnUpdParam();
//        p.setIsemester(isemester);
////        p.setStudentNum(studentNum);
//        return SERVICE.updGradeMngmn(p);
//    }
//
    @GetMapping
    @Operation(summary = " 학번으로 조회")
    public GradeMngmnFindRes getGradeMngmn(@ParameterObject @PageableDefault(sort = "student_num", direction = Sort.Direction.DESC) Pageable pageable,
                                           @RequestParam(required = false, defaultValue = "1") int grade,
                                           @RequestParam Long studentNum) {

        GradeMngmnDto dto = new GradeMngmnDto();
        dto.setStudentNum(studentNum);
        dto.setGrade(grade);
        dto.setSize(pageable.getPageSize());
        dto.setPage(pageable.getPageNumber());
        return SERVICE.selGradeMngmn(dto, pageable);
    }

    @GetMapping("/{studentNum}")
    @Operation(summary = "상세보기")
    public Optional<GradeMngmnDetailVo> getGradeMngmnDetail(@PathVariable Long studentNum) {
        GradeMngmnDetailSelDto dto = new GradeMngmnDetailSelDto();
        dto.setStudentNum(studentNum);
        return SERVICE.selStudentDetail(dto);
    }

    @PatchMapping
    @Operation(summary = "SEMESTER_SCORE 일괄입력 (학기말에 한번에 하는)")
    public GradeMngmnUpdRes putGradeMngmn2(@RequestParam Long isemester) {
        GradeMngmnUpdParam p = new GradeMngmnUpdParam();
        p.setIsemester(isemester);
        return SERVICE.updGradeMngmn2(p);
    }
}
