package com.green.smartgradever2.admin.professor;

import com.green.smartgradever2.admin.professor.model.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "관리자 교수 관리")
public class AdminProfessorController {

    private final AdminProfessorService SERVICE;


    @PostMapping("/professor")
    @Operation(summary = "교수등록")
    public ResponseEntity<List<AdminProfessorInsVo>> professorEnrollment(@RequestBody List<AdminProfessorInsParam> param){
        List<AdminProfessorInsVo> res = SERVICE.insProfessor(param);
        return ResponseEntity.ok().body(res);
    }


    @GetMapping("/professor")
    @Operation(summary = "교수 검색")
    public ResponseEntity<AdminProfessorFindRes> findP(@ParameterObject @PageableDefault(sort = "iprofessor",direction = Sort.Direction.DESC) Pageable page, @RequestParam(required = false) String name, @RequestParam (required = false,defaultValue = "0")Long imajor) {

        AdminProfessorFindParam param = new AdminProfessorFindParam();
        param.setImajor(imajor);
        param.setName(name);

        AdminProfessorFindRes res = SERVICE.findProfessors(page, param);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/professor/{iprofessor}")
    @Operation(summary = "교수 상세보기")
    public AdminProfessorDetailRes findProfessorDetail(@PathVariable Long iprofessor) {

        return SERVICE.findProfessorDetail(iprofessor);
    }

    @PatchMapping("/professor/{iprofessor}")
    @Operation(summary = "교수 정보 수정")
    public ResponseEntity<AdminProfessorInsVo> patchProfessor(@PathVariable Long iprofessor,@RequestBody AdminProfessorPatchParam param) throws Exception {

        AdminProfessorInsVo vo = SERVICE.patchProfessor(iprofessor, param);

       return ResponseEntity.ok().body(vo);
    }

    @GetMapping("/professor-file")
    @Operation(summary = "대학 교수 구성원들 정보")
    public void greenUniversityMember(HttpServletResponse request) throws IOException {
        SERVICE.professorListFile(request);
    }

}
