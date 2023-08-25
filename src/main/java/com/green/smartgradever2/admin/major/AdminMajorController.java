package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorDto;
import com.green.smartgradever2.admin.major.model.AdminMajorSaveDto;
import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.entity.MajorEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/major")
@Tag(name = "관리자 전공 관리")
public class AdminMajorController {

    private final AdminMajorService SERVICE;

    @PostMapping
    @Operation(summary = "전공 추가")
    public Long postMajor(@RequestBody AdminMajorSaveDto dto) {
        MajorEntity entity = new MajorEntity();
        entity.setMajorName(dto.getMajorName());
        entity.setGraduationScore(dto.getGraduationScore());
        entity.setRemarks("");
        return SERVICE.insMajor(entity);
    }

    @GetMapping
    public List<AdminMajorVo> getMajor(@PageableDefault(sort = "imajor", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
                                       @RequestParam (required = false) String majorName,
                                       @RequestParam (required = false, defaultValue = "0") int delYn) {
        AdminMajorDto dto = new AdminMajorDto();
        dto.setMajorName(majorName);
        dto.setDelYn(delYn);
        dto.setPage(pageable.getPageNumber());
        dto.setSize(pageable.getPageSize());
        return SERVICE.selMajor(dto,pageable);
    }


    @DeleteMapping
    @Operation(summary = "전공 폐지 ( del_yn 0 1 변경 )")
    public AdminMajorVo delMajor(@RequestParam Long imajor) {
        MajorEntity entity = new MajorEntity();
        entity.setImajor(imajor);
        return SERVICE.delMajor(entity);
    }

    @PatchMapping
    @Operation(summary = "전공 이름 수정")
    public AdminMajorVo patchMajor(@RequestBody MajorEntity entity) {
        return SERVICE.updMajor(entity);
    }
}
