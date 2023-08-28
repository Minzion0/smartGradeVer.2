package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.model.AdminInsSemesterParam;
import com.green.smartgradever2.admin.model.AdminInsSemesterVo;
import com.green.smartgradever2.admin.model.AdminLectureInsNameParam;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.entity.SemesterEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository RPS;
    private final SemesterRepository SEMESTER_RPS;
    private final AdminMapper MAPPER;
    private final EntityManager EM;

    @Transactional(rollbackFor = Exception.class)
    public AdminInsSemesterVo semesterIns(AdminInsSemesterParam param){

        SemesterEntity semesterEntity = new SemesterEntity();
            semesterEntity.setSemester(param.getSemester());
            semesterEntity.setSemesterStrDate(param.getSemesterStrDate());
            semesterEntity.setSemesterEndDate(param.getSemesterEndDate());
            LocalDate year = LocalDate.parse(param.getYear());
            semesterEntity.setYear(year);
            semesterEntity.setLectureApplyDeadline(param.getLectureApplyDeadline());

        SEMESTER_RPS.save(semesterEntity);

        EM.clear();

        SemesterEntity semester = SEMESTER_RPS.findById(semesterEntity.getIsemester()).get();

        AdminInsSemesterVo vo = new AdminInsSemesterVo();
            vo.setIsemester(semester.getIsemester());
            vo.setSemester(semester.getSemester());
            vo.setSemesterStrDate(semester.getSemesterStrDate());
            vo.setSemesterEndDate(semester.getSemesterEndDate());
            vo.setDelYn(semester.getDelYn());
        return vo;
    }

    private void insLectureName(AdminLectureInsNameParam param){

    }
}
