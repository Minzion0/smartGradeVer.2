package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.model.AdminInsSemesterParam;
import com.green.smartgradever2.admin.model.AdminInsSemesterVo;
import com.green.smartgradever2.admin.model.AdminLectureInsNameParam;
import com.green.smartgradever2.admin.model.AdminLectureInsNameVo;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.entity.LectureNameEntity;
import com.green.smartgradever2.entity.SemesterEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository RPS;
    private final SemesterRepository SEMESTER_RPS;
    private final AdminMapper MAPPER;
    private final EntityManager EM;
    private final LectureNameRepository LECTURE_NM_RPS;

    @Transactional(rollbackFor = Exception.class)
    public AdminInsSemesterVo semesterIns(AdminInsSemesterParam param){
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        SemesterEntity semesterEntity = new SemesterEntity();
            semesterEntity.setSemester(param.getSemester());
            semesterEntity.setSemesterStrDate(param.getSemesterStrDate());
            semesterEntity.setSemesterEndDate(param.getSemesterEndDate());

            semesterEntity.setYear(param.getSemesterStrDate().getYear());
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
            vo.setLectureApplyDeadline(semester.getLectureApplyDeadline());
        return vo;
    }
    @Transactional(rollbackFor = Exception.class)
    public AdminLectureInsNameVo insLectureName(AdminLectureInsNameParam param) throws Exception{
        if (param.getScore() <= 0){
            throw new Exception("학점은 1점 이상이여야 합니다");
        }

        LectureNameEntity lectureName = new LectureNameEntity();
        lectureName.setLectureName(param.getLectureName());
        lectureName.setScore(param.getScore());

        try {
            LECTURE_NM_RPS.save(lectureName);
        }catch (Exception e){
            throw new Exception("강의명이 이미 존제합니다 ");
        }

        EM.clear();

        LectureNameEntity nameEntity = LECTURE_NM_RPS.findById(lectureName.getIlectureName()).get();

        AdminLectureInsNameVo vo = new AdminLectureInsNameVo();
        vo.setIlectureName(nameEntity.getIlectureName());
        vo.setScore(nameEntity.getScore());
        vo.setLectureName(nameEntity.getLectureName());

        return vo;

    }
}
