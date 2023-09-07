package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.lecturecondition.LectureConditionRepository;
import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.admin.model.*;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.admin.student.AdminStudentRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.config.exception.AdminException;

import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.awt.Color.*;

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
    private final AdminStudentRepository STUDENT_RPS;
    private final AdminMajorRepository MAJOR_RPS;
    private final AdminQdsl adminQdsl;


    /**학기 등록**/
    @Transactional(rollbackFor = Exception.class)
    public AdminInsSemesterVo semesterIns(AdminInsSemesterParam param) {
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
    /**강의명 등록**/
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
    /**강의명 검색**/
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
        Optional<LectureApplyEntity> optionalLectureApplyEntity = APPLY_RPS.findById(ilecture);
        if (optionalLectureApplyEntity.isEmpty()) {
            throw new AdminException("존제하지 않는 강의 입니다");
        }
        LectureApplyEntity lectureApplyEntity = optionalLectureApplyEntity.get();
        List<LectureStudentEntity> applyEntity = LECTURE_STUDENT_RPS.findByLectureApplyEntity(apply);


            if (lectureApplyEntity.getOpeningProceudres()==0) {
                LectureConditionEntity entity = LECTURE_CONDITION_RPS.findById(ilecture).get();
                AdminLectureConditionVo vo = new AdminLectureConditionVo();
                vo.setIlecture(entity.getIlecture().getIlecture());
                vo.setReturnCtnt(entity.getReturnCtnt());
                vo.setReturnDate(entity.getReturnDate());
                return ResponseEntity.ok().body(vo);
            }

        //3차로 넘어오면서 성적이아닌 강의 정보를 보여주기로 한다
//        List<AdminLectureStudentVo> vo = applyEntity.stream().map(student -> {
//                    GradeUtils gradeUtils = new GradeUtils(student.getTotalScore());
//                    double score = gradeUtils.totalScore();
//                    String rating = gradeUtils.totalRating(score);
//                    return AdminLectureStudentVo.builder()
//                            .istudent(student.getStudentEntity().getStudentNum())
//                            .nm(student.getStudentEntity().getNm())
//                            .gread(rating)
//                            .majorNm(student.getStudentEntity().getMajorEntity().getMajorName())
//                            .attendance(student.getAttendance())
//                            .minEx(student.getMidtermExamination())
//                            .finEx(student.getFinalExamination())
//                            .totalScore(student.getTotalScore())
//                            .avg(score).build()
//                            ;
//                }
//        ).toList();




        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime lectureStrTime = lectureApplyEntity.getLectureScheduleEntity().getLectureStrTime();
        LocalTime lectureEndTime = lectureApplyEntity.getLectureScheduleEntity().getLectureEndTime();
        String passStrTime = lectureStrTime.format(formatter);
        String passEndTime = lectureEndTime.format(formatter);

        AdminLectureDetailVo vo = AdminLectureDetailVo.builder()
                .lectureName(lectureApplyEntity.getLectureNameEntity().getLectureName())
                .lectureRoomName(lectureApplyEntity.getLectureRoomEntity().getLectureRoomName())
                .buildingName(lectureApplyEntity.getLectureRoomEntity().getBuildingName())
                .currentPeople(applyEntity.size())
                .dayWeek(lectureApplyEntity.getLectureScheduleEntity().getDayWeek())
                .year(lectureApplyEntity.getSemesterEntity().getSemesterStrDate().toString().substring(0,4))
                .score(lectureApplyEntity.getLectureNameEntity().getScore())
                .lectureStrDate(lectureApplyEntity.getSemesterEntity().getSemesterStrDate())
                .lectureEndDate(lectureApplyEntity.getSemesterEntity().getSemesterEndDate())
                .lectureStrTime(passStrTime)
                .lectureEndTime(passEndTime)
                .gradeLimit(lectureApplyEntity.getGradeLimit())
                .attendance(lectureApplyEntity.getAttendance())
                .midtermExamination(lectureApplyEntity.getMidtermExamination())
                .finalExamination(lectureApplyEntity.getFinalExamination())
                .textBook(lectureApplyEntity.getTextbook())
                .ctnt(lectureApplyEntity.getCtnt())
                .bookUrl(lectureApplyEntity.getBookUrl())
                .build();


        return ResponseEntity.ok().body(vo);
    }
    /**강의 리스트 확인**/

    public AdminSelRes selLecture(AdminSelLectureParam param, Pageable page) {
        AdminSelLectureDto dto = new AdminSelLectureDto(param);


        Page<AdminSelLectureVo> adminSelLectureVos = adminQdsl.selLecture(dto, page);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");


        List<AdminSelLectureRes> list = adminSelLectureVos.stream().map(vos -> AdminSelLectureRes.builder().
                ilecture(vos.getIlecture())
                .lectureNm(vos.getLectureNm())
                .semester(vos.getSemester())
                .majorName(vos.getMajorName())
                .year(vos.getStrDate().toString().substring(0,4))
                .nm(vos.getNm())
                .dayWeek(vos.getDayWeek())
                .lectureRoomNm(vos.getLectureRoomNm())
                .buildingNm(vos.getBuildingNm())
                .gradeLimit(vos.getGradeLimit())
                .score(vos.getScore())
                .strDate(vos.getStrDate())
                .endDate(vos.getEndDate())
                .strTime(vos.getStrTime().format(dateTimeFormatter))
                .endTime(vos.getEndTime().format(dateTimeFormatter))
                .maxPeople(vos.getMaxPeople())
                .currentPeople(vos.getCurrentPeople())
                .procedures(vos.getProcedures())
                .delYn(vos.getDelYn()).build()).toList();


        PagingUtils pagingUtils = new PagingUtils(page.getPageNumber(),(int)adminSelLectureVos.getTotalElements());

        return AdminSelRes.builder().lectures(list).page(pagingUtils).build();

    }
/**강의 상태 변경**/
    public AdminUpdLectureRes lectureModify(AdminUpdLectureDto dto) {

        Optional<LectureApplyEntity> optionalLectureApplyEntity = APPLY_RPS.findById(dto.getIlecture());


        if (optionalLectureApplyEntity.isEmpty()) {
            throw new AdminException("해당 강의가 없습니다");
        }
        LectureApplyEntity applyEntity = optionalLectureApplyEntity.get();

        applyEntity.setOpeningProceudres(dto.getProcedures());
        if (dto.getProcedures() == 0) {
            LectureConditionEntity entity = new LectureConditionEntity();
            entity.setIlecture(applyEntity);
            entity.setReturnCtnt(dto.getCtnt());
            LectureConditionEntity save = LECTURE_CONDITION_RPS.save(entity);
            return AdminUpdLectureRes.builder().ilecture(save.getIlecture().getIlecture()).ctnt(save.getReturnCtnt()).procedures(save.getIlecture().getOpeningProceudres()).build();
        }
        if (dto.getProcedures() == 2) {
            LocalDate lectureApplyDeadline = applyEntity.getSemesterEntity().getLectureApplyDeadline();
            LocalDate applyDeadline = lectureApplyDeadline.plusWeeks(2);
            applyEntity.setStudentsApplyDeadline(applyDeadline);
        }

        APPLY_RPS.save(applyEntity);

        return AdminUpdLectureRes.builder().ilecture(applyEntity.getIlecture()).ctnt(applyEntity.getCtnt()).procedures(applyEntity.getOpeningProceudres()).build();
    }


}
