package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.*;
import com.green.smartgradever2.config.entity.MajorEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "관리자 전공 관리")
public class AdminMajorController {

    private final AdminMajorService SERVICE;

    @PostMapping("/admin/major")
    @Operation(summary = "전공 INSERT")
    public Long postMajor(@RequestBody AdminMajorSaveDto dto) {
        MajorEntity entity = new MajorEntity();
        entity.setMajorName(dto.getMajorName());
        entity.setGraduationScore(dto.getGraduationScore());
        entity.setRemarks("");
        return SERVICE.insMajor(entity);
    }

    @GetMapping("/admin/major")
    @Operation(summary = "전공 리스트 SELECT")
    public ResponseEntity<AdminMajorFindRes> getMajor(@ParameterObject @PageableDefault(sort = "imajor", direction = Sort.Direction.ASC) Pageable pageable,
                                      @RequestParam (required = false) String majorName,
                                      @RequestParam (required = false) Integer delYn) {
        AdminMajorDto dto = new AdminMajorDto();
        dto.setMajorName(majorName);
        dto.setDelYn(delYn);
        dto.setPage(pageable.getPageNumber());
        dto.setSize(pageable.getPageSize());
        return SERVICE.selMajor(dto,pageable);
    }


    @DeleteMapping("/admin/major")
    @Operation(summary = "전공 폐지 ( del_yn 0 1 변경 )")
    public AdminMajorVo delMajor(@RequestParam Long imajor) {
        MajorEntity entity = new MajorEntity();
        entity.setImajor(imajor);
        return SERVICE.delMajor(entity);
    }

    @PatchMapping("/admin/major")
    @Operation(summary = "전공 이름 수정")
    public AdminMajorVo patchMajor(@RequestBody AdminMajorPatchDto dto) {
        MajorEntity entity = new MajorEntity();
        entity.setMajorName(dto.getMajorName());
        entity.setImajor(dto.getImajor());
        entity.setGraduationScore(dto.getGraduationScore());
        return SERVICE.updMajor(entity);
    }

    @GetMapping("/major/list")
    @Operation(summary = "전공 x페이징")
    public ResponseEntity<List<MajorListVo>> getMajorList(){
        List<MajorListVo> majorList = SERVICE.getMajorList();
        return ResponseEntity.ok().body(majorList);
    }
}
