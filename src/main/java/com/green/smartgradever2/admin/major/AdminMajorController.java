package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.entity.MajorEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/major")
@Tag(name = "관리자 전공 관리")
public class AdminMajorController {

    private final AdminMajorService SERVICE;

    @PostMapping
    @Operation(summary = "전공 추가")
    public Long postMajor(MajorEntity entity) {
        return SERVICE.insMajor(entity);
    }

    @PatchMapping
    @Operation(summary = "전공 삭제 ( del_yn 0 1 변경 )")
    public AdminMajorVo patchMajor(@RequestParam Long imajor) {
        MajorEntity entity = new MajorEntity();
        entity.setImajor(imajor);
        return SERVICE.delMajor(entity);
    }
}
