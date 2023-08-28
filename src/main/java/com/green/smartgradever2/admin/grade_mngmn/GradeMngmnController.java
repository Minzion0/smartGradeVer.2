package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnDetailSelDto;
import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnDetailVo;
import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnDto;
import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnRes;
import com.green.smartgradever2.admin.major.AdminMajorService;
import com.green.smartgradever2.entity.StudentEntity;
import com.green.smartgradever2.entity.StudentSemesterScoreEntity;
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

    @GetMapping
    public GradeMngmnRes getGradeMngmn(@PageableDefault(sort="student_num", direction = Sort.Direction.DESC) Pageable pageable,
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
    public GradeMngmnDetailVo getGradeMngmnDetail(@PathVariable Integer studentNum) {
        GradeMngmnDetailSelDto dto = new GradeMngmnDetailSelDto();
        dto.setStudentNum(studentNum);
        return SERVICE.selStudentDetail(dto);
    }
}
