package com.green.smartgradever2.professor;

import com.green.smartgradever2.professor.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor")
public class ProfessorController {
    private  final  ProfessorService SERVICE;


    @GetMapping("/{iprofessor}")
    public ProfessorProfileDto getProfessorProfile(@PathVariable Long iprofessor) {
        return SERVICE.getProfessorProfile(iprofessor);
    }


    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "교수 프로필 수정")
    public ResponseEntity<ProfessorUpRes> updateProfessorProfile(
            @RequestPart(required = false) MultipartFile pic,

            @RequestPart ProfessorUpdDto dto) {

        ProfessorParam param = new ProfessorParam();
        param.setPhone(dto.getPhone());
        param.setEmail(dto.getEmail());
        param.setAddress(dto.getAddress());
        param.setIprofessor(dto.getIprofessor());

        try {
            // 기존 사진 삭제 처리
            SERVICE.processProfessorPicDeletion(dto.getIprofessor());

            // 교수 정보 및 사진 업데이트 처리
            ProfessorUpRes response = SERVICE.upProfessor(pic, param);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
