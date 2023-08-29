package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.lecturecondition.LectureConditionRepository;
import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.model.*;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.config.exception.AdminException;

import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final AdminRepository RPS;
    private final SemesterRepository SEMESTER_RPS;
    private final AdminMapper MAPPER;
    private final EntityManager EM;
    private final LectureNameRepository LECTURE_NM_RPS;
    private final LectureStudentRepository LECTURE_STUDENT_RPS;
    private final LectureConditionRepository LECTURE_CONDITION_RPS;
    private final LectureApplyRepository APPLY_RPS;


    @Transactional(rollbackFor = Exception.class)
    public AdminInsSemesterVo semesterIns(AdminInsSemesterParam param)  {
        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setSemester(param.getSemester());
        semesterEntity.setSemesterStrDate(param.getSemesterStrDate());
        semesterEntity.setSemesterEndDate(param.getSemesterEndDate());

        semesterEntity.setYear(param.getSemesterStrDate().getYear());
        semesterEntity.setLectureApplyDeadline(param.getLectureApplyDeadline());
        try {

            SEMESTER_RPS.save(semesterEntity);
        } catch (Exception e) {
            throw new AdminException("중복 학기 입니다");
        }

        EM.clear();

        SemesterEntity semester = SEMESTER_RPS.findById(semesterEntity.getIsemester()).get();

        AdminInsSemesterVo vo = new AdminInsSemesterVo();
        vo.setIsemester(semester.getIsemester());
        vo.setSemester(semester.getSemester());
        vo.setSemesterStrDate(semester.getSemesterStrDate());
        vo.setSemesterEndDate(semester.getSemesterEndDate());
        vo.setDelYn(semester.getDelYn());
        vo.setYear(semester.getYear());
        vo.setLectureApplyDeadline(semester.getLectureApplyDeadline());
        return vo;
    }

    @Transactional(rollbackFor = AdminException.class)
    public AdminLectureInsNameVo insLectureName(AdminLectureInsNameParam param) {
        if (param.getScore() <= 0) {
            throw new AdminException("학점은 1점 이상이여야 합니다");
        }

        LectureNameEntity lectureName = new LectureNameEntity();
        lectureName.setLectureName(param.getLectureName());
        lectureName.setScore(param.getScore());

        try {
            LECTURE_NM_RPS.save(lectureName);
        } catch (Exception e) {
            throw new AdminException("강의명이 이미 존제합니다 ");
        }

        EM.clear();

        LectureNameEntity nameEntity = LECTURE_NM_RPS.findById(lectureName.getIlectureName()).get();

        AdminLectureInsNameVo vo = new AdminLectureInsNameVo();
        vo.setIlectureName(nameEntity.getIlectureName());
        vo.setScore(nameEntity.getScore());
        vo.setLectureName(nameEntity.getLectureName());

        return vo;

    }

    public List<AdminLectureNameFindVo> findLectureName(String lectureName) {
        List<LectureNameEntity> regex = null;
        if (lectureName != null) {
            regex = LECTURE_NM_RPS.findByLectureNameContains(lectureName);
        }
        if (lectureName == null) {
            regex = LECTURE_NM_RPS.findAll();
        }

        return regex.stream().map(entity -> AdminLectureNameFindVo.builder()
                .ilectureName(entity.getIlectureName())
                .score(entity.getScore())
                .delYn(entity.getDelYn())
                .lectureName(entity.getLectureName()).build()).toList();
    }

    public List<AdminSemesterFindVo> findSemester(Integer year) {
        List<SemesterEntity> list = null;
        if (year != null) {
            list = SEMESTER_RPS.findByYear(year);
        }
        if (year == null) {
            list = SEMESTER_RPS.findAll();
        }

        return list.stream().map(semesterEntity -> AdminSemesterFindVo.builder()
                .isemester(semesterEntity.getIsemester())
                .semesterStrDate(semesterEntity.getSemesterStrDate())
                .semesterEndDate(semesterEntity.getSemesterEndDate())
                .semester(semesterEntity.getSemester())
                .year(semesterEntity.getYear())
                .lectureApplyDeadline(semesterEntity.getLectureApplyDeadline()).build()).toList();
    }

    public ResponseEntity<?> findLectureStudent(Long ilecture) {
        LectureApplyEntity apply = new LectureApplyEntity();
        apply.setIlecture(ilecture);
        List<LectureStudentEntity> applyEntity = LECTURE_STUDENT_RPS.findByLectureApplyEntity(apply);

        if (applyEntity.get(0).getLectureApplyEntity().getOpeningProceudres() == 0) {
            LectureConditionEntity entity = LECTURE_CONDITION_RPS.findById(ilecture).get();
            AdminLectureConditionVo vo = new AdminLectureConditionVo();
            vo.setIlecture(entity.getIlecture().getIlecture());
            vo.setReturnCtnt(entity.getReturnCtnt());
            vo.setReturnDate(entity.getReturnDate());
            return ResponseEntity.ok().body(entity);
        }
        List<AdminLectureStudentVo> vo = applyEntity.stream().map(student -> {
                    GradeUtils gradeUtils = new GradeUtils(student.getTotalScore());
                    double score = gradeUtils.totalScore();
                    String rating = gradeUtils.totalRating(score);
                    return AdminLectureStudentVo.builder()
                            .istudent(student.getStudentEntity().getStudentNum())
                            .nm(student.getStudentEntity().getNm())
                            .gread(rating)
                            .majorNm(student.getStudentEntity().getMajorEntity().getMajorName())
                            .attendance(student.getAttendance())
                            .minEx(student.getMidtermExamination())
                            .finEx(student.getFinalExamination())
                            .totalScore(student.getTotalScore())
                            .avg(score).build()
                            ;
                }
        ).toList();

        return ResponseEntity.ok().body(vo);
    }

    public AdminSelRes selLecture(AdminSelLectureParam param, Pageable page) {
        AdminSelLectureDto dto = new AdminSelLectureDto(param);
        int maxpage = MAPPER.countLceture(dto);
            PagingUtils utils = new PagingUtils(page.getPageNumber(), maxpage);
            dto.setRow(utils.getROW());
            dto.setStrIdx(utils.getStaIdx());
            List<AdminSelLectureVo> res = MAPPER.selLecture(dto);

        for (AdminSelLectureVo re : res) {
            int str = re.getStrTime().lastIndexOf(":");
            re.getStrTime().substring(str);
            re.getEndTime();
        }

        return AdminSelRes.builder().lectures(res).page(utils).build();

    }

    public AdminUpdLectureRes lectureModify(AdminUpdLectureDto dto){

        Optional<LectureApplyEntity> optionalLectureApplyEntity = APPLY_RPS.findById(dto.getIlecture());


        if (optionalLectureApplyEntity.isEmpty()) {
            throw new AdminException("해당 강의가 없습니다");
        }
        LectureApplyEntity applyEntity = optionalLectureApplyEntity.get();

        applyEntity.setOpeningProceudres(dto.getProcedures());
        if (dto.getProcedures()==0){
            LectureConditionEntity entity = new LectureConditionEntity();
            entity.setIlecture(applyEntity);
            entity.setReturnCtnt(dto.getCtnt());
            LectureConditionEntity save = LECTURE_CONDITION_RPS.save(entity);
            return AdminUpdLectureRes.builder().ilecture(save.getIlecture().getIlecture()).ctnt(save.getReturnCtnt()).procedures(save.getIlecture().getOpeningProceudres()).build();
        }

        applyEntity.setOpeningProceudres(dto.getProcedures());
        APPLY_RPS.save(applyEntity);

        return AdminUpdLectureRes.builder().ilecture(applyEntity.getIlecture()).ctnt(applyEntity.getCtnt()).procedures(applyEntity.getOpeningProceudres()).build();
    }

}
