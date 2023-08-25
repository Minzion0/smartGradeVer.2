package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorDto;
import com.green.smartgradever2.admin.major.model.AdminMajorSaveDto;
import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.entity.MajorEntity;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminMajorService {

    private final AdminMajorRepository MAJOR_REP;

    /** 전공 INSERT **/
    public Long insMajor(MajorEntity entity) {
        MajorEntity result = MAJOR_REP.save(entity);
        if (result == null) {
            return 0L;
        }
        return result.getImajor();
    }

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

    public List<AdminMajorVo> selMajor(AdminMajorDto dto, Pageable pageable) {
        int maxPage = pageable.getPageSize();
        PagingUtils utils = new PagingUtils(dto.getPage(),maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<MajorEntity> list = MAJOR_REP.findAllByDelYnAndMajorName(dto.getDelYn(), dto.getMajorName());
        return list.stream()
                .map(item -> AdminMajorVo.builder()
                .delYn(item.getDelYn())
                .majorName(item.getMajorName())
                .remarks(item.getRemarks())
                .graduationScore(item.getGraduationScore())
                .imajor(item.getImajor())
                .build())
                .toList();
    }

    public AdminMajorVo updMajor(MajorEntity entity) {
        Optional<MajorEntity> byId = MAJOR_REP.findById(entity.getImajor());

        if (byId.isPresent()) {
            MajorEntity majorEntity = byId.get();
            majorEntity.setMajorName(entity.getMajorName());

            MajorEntity save = MAJOR_REP.save(majorEntity);

            if (!(save.getMajorName().equals(entity.getMajorName()))) {
                return AdminMajorVo.builder()
                        .majorName(save.getMajorName())
                        .remarks(entity.getMajorName())
                        .build();
            } else {
                throw new RuntimeException("이미 존재하는 학과 입니다.");
            }
        } else {
            throw new EntityNotFoundException("찾을 수 없는 pk 입니다.");
        }

    }
}
