package com.green.smartgradever2.professor;

import com.green.smartgradever2.professor.model.*;
import com.green.smartgradever2.settings.security.config.security.model.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor")
@Tag(name = "교수")
public class ProfessorController {
    private  final  ProfessorService SERVICE;


//    @GetMapping("/{iprofessor}")
//    @Operation(summary = "교수 프로필")
//    public ProfessorProfileDto getProfessorProfile(@PathVariable Long iprofessor) {
//        return SERVICE.getProfessorProfile(iprofessor);
//    }
    @GetMapping
    @Operation(summary = "교수프로필 본인 강의까지 출력")
    public ProfessorSelRes getProfessorWithLectures(@RequestParam Long iprofessor) {

        return SERVICE.getProfessorLectures(iprofessor);
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
    public ProfessorSelLectureRes getLecturePro(@RequestParam Long iprofessor
            ,@RequestParam (defaultValue = "1") int page
            ,@RequestParam(required = false ) String openingProcedures) {
        ProfessorSelLectureDto dto = new ProfessorSelLectureDto();
        dto.setIprofessor(iprofessor);
        dto.setPage(page);
        dto.setOpeningProcedures(openingProcedures);
        return SERVICE.selProfessorLecture(dto);
    }

    @PutMapping("/changPassword")
    @Operation(summary = "비밀번호 변경",
            description = "currentProfessorPassword : 현재 비밀번호 <br>" + "professorPassword : 바꿀 비밀번호")
    public ResponseEntity<?> updPassword(@AuthenticationPrincipal MyUserDetails details,
                                         @RequestBody ProfessorUpdPasswordParam param) throws Exception {
        ProfessorUpdPasswordDto dto = new ProfessorUpdPasswordDto();
        Long iuser = details.getIuser();
        String role = details.getRoles().get(0);
        dto.setIprofessor(iuser);
        dto.setRole(role);
        return ResponseEntity.ok().body(SERVICE.updPassword(dto, param));
    }

}
