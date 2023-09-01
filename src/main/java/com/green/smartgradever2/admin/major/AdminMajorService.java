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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminMajorService {

    private final AdminMajorRepository MAJOR_REP;
    private final AdminMajorMapper MAPPER;

    /**
     * 전공 INSERT
     **/
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

    public AdminMajorFindRes selMajor(AdminMajorDto dto) {
        int maxPage = MAPPER.countMajor();
        PagingUtils utils = new PagingUtils(dto.getPage(), maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<AdminMajorVo> list = MAPPER.selMajor(dto);

        return AdminMajorFindRes.builder()
                .paging(utils)
                .vo(list)
                .build();
    }


    public AdminMajorVo updMajor(MajorEntity entity) {
        Optional<MajorEntity> byId = MAJOR_REP.findById(entity.getImajor());

        if (byId.isPresent()) {
            MajorEntity majorEntity = byId.get();

            MajorEntity save;
            if (!(entity.getMajorName().equals(byId.get().getMajorName()))) {
                majorEntity.setRemarks("구 " + byId.get().getMajorName());
                majorEntity.setMajorName(entity.getMajorName());
                save = MAJOR_REP.save(majorEntity);
            } else {
                throw new RuntimeException("이미 존재하는 학과입니다.");
            }
            return AdminMajorVo.builder()
                    .majorName(save.getMajorName())
                    .remarks(entity.getMajorName())
                    .build();
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
