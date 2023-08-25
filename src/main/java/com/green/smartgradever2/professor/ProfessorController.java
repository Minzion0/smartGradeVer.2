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
    @GetMapping
    @Operation(summary = "교수프로필 본인 강의까지 출력")
    public ProfessorSelRes getProfessorWithLectures(@RequestParam Long iprofessor) {
        ProfessorProfileDto dto = new ProfessorProfileDto();
        dto.setIprofessor(iprofessor);
        return SERVICE.getProfessorLectures(dto);
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
    @GetMapping("/lecture-List")
    @Operation(summary = "본인의 강의 조회")
    public ProfessorLctureSelRes getLecturePro(@RequestParam Long iprofessor
            ,@RequestParam (defaultValue = "1") int page
            ,@RequestParam(required = false ) String openingProcedures) {
        ProfessorSelLectureDto dto = new ProfessorSelLectureDto();
        dto.setIprofessor(iprofessor);
        dto.setPage(page);
        dto.setOpeningProcedures(openingProcedures);
        return SERVICE.selProfessorLecture(dto);
    }


}
