package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.entity.MajorEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
