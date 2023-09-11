package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorDto;
import com.green.smartgradever2.admin.major.model.AdminMajorFindRes;
import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.admin.major.model.MajorListVo;
import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminMajorService {

    private final AdminMajorRepository MAJOR_REP;
    private final AdminMajorMapper MAPPER;
    private final AdminMajorQdsl adminMajorQdsl;

    /** 전공 INSERT **/
    public Long insMajor(MajorEntity entity) {
        MajorEntity result = MAJOR_REP.save(entity);
        if (result == null) {
            return 0L;
        }
        return result.getImajor();
    }

    /** 전공 DELETE **/
    public AdminMajorVo delMajor(MajorEntity entity) {
        Optional<MajorEntity> byId = MAJOR_REP.findById(entity.getImajor());

        if (byId.isPresent()) {
            MajorEntity majorEntity = byId.get();
            majorEntity.setDelYn(1);

            MajorEntity save = MAJOR_REP.save(majorEntity);
            return AdminMajorVo.builder()
                    .delYn(save.getDelYn())
                    .build();
        } else {
            throw new EntityNotFoundException("not found");
        }
    }
//    public ResponseEntity<AdminMajorFindRes> selMajor(AdminMajorDto dto, Pageable pageable) {
//        long maxPage = MAJOR_REP.count();
//        PagingUtils utils = new PagingUtils(pageable.getPageNumber(), (int)maxPage);
//        dto.setStaIdx(utils.getStaIdx());
//
//        List<AdminMajorVo> major = MAPPER.selMajor(dto);
//        AdminMajorFindRes build = AdminMajorFindRes.builder()
//                .vo(major)
//                .page(utils)
//                .build();
//        return ResponseEntity.ok(build);
//    }

    /** 전공 SELECT **/
    public ResponseEntity<AdminMajorFindRes> selMajor(AdminMajorDto dto, Pageable pageable) {
        long maxPage = MAJOR_REP.count();
        PagingUtils utils = new PagingUtils(dto.getPage(), (int)maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<AdminMajorVo> list = adminMajorQdsl.majorVos(dto, pageable);

        AdminMajorFindRes build = AdminMajorFindRes.builder()
                .page(utils)
                .vo(list.stream().map(item -> AdminMajorVo.builder()
                        .majorName(item.getMajorName())
                        .graduationScore(item.getGraduationScore())
                        .imajor(item.getImajor())
                        .delYn(item.getDelYn())
                        .remarks(item.getRemarks())
                        .build()).toList())
                .build();
        return ResponseEntity.ok(build);
    }

//    public AdminMajorFindRes selMajor2(AdminMajorDto dto, Pageable pageable) {
//        long maxPage = MAJOR_REP.count();
//        PagingUtils utils = new PagingUtils(dto.getPage(), (int)maxPage);
//        dto.setStaIdx(utils.getStaIdx());
//
//        List<AdminMajorVo> list = adminMajorQdsl.majorVos(dto, pageable);
//
//        return AdminMajorFindRes.builder()
//                .paging(utils)
//                .vo(list)
//                .build();
//    }


    public AdminMajorVo updMajor(MajorEntity entity) {
        Optional<MajorEntity> byId = MAJOR_REP.findById(entity.getImajor());

        if (byId.isPresent()) {
            MajorEntity majorEntity = byId.get();

            MajorEntity save;
            if (!(entity.getMajorName().equals(byId.get().getMajorName()))) {
                majorEntity.setRemarks("구 " + byId.get().getMajorName());
                majorEntity.setMajorName(entity.getMajorName());
                majorEntity.setGraduationScore(entity.getGraduationScore());
                save = MAJOR_REP.save(majorEntity);

                return AdminMajorVo.builder()
                        .imajor(entity.getImajor())
                        .majorName(save.getMajorName())
                        .remarks(entity.getMajorName())
                        .graduationScore(entity.getGraduationScore())
                        .build();
            } else {
                majorEntity.setGraduationScore(entity.getGraduationScore());
                save = MAJOR_REP.save(majorEntity);
                return AdminMajorVo.builder()
                        .majorName(entity.getMajorName())
                        .remarks(entity.getRemarks())
                        .imajor(entity.getImajor())
                        .graduationScore(save.getGraduationScore())
                        .build();
            }

        } else {
            throw new EntityNotFoundException("찾을 수 없는 pk 입니다.");
        }
    }


    public List<MajorListVo> getMajorList(){
        List<MajorEntity> majorEntityList = MAJOR_REP.findAll();

        return majorEntityList.stream().map(list -> MajorListVo.builder()
                .imajor(list.getImajor())
                .majorName(list.getMajorName())
                .build()).toList();
    }
}
