package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.lecturecondition.LectureConditionRepository;
import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.model.*;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.admin.student.AdminStudentRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.config.exception.AdminException;

import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

        if (applyEntity.size()!=0){
            if (applyEntity.get(0).getLectureApplyEntity().getOpeningProceudres() == 0) {
                LectureConditionEntity entity = LECTURE_CONDITION_RPS.findById(ilecture).get();
                AdminLectureConditionVo vo = new AdminLectureConditionVo();
                vo.setIlecture(entity.getIlecture().getIlecture());
                vo.setReturnCtnt(entity.getReturnCtnt());
                vo.setReturnDate(entity.getReturnDate());
                return ResponseEntity.ok().body(entity);
            }
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

        Optional<LectureApplyEntity> optionalLectureApplyEntity = APPLY_RPS.findById(ilecture);
        if (optionalLectureApplyEntity.isEmpty()){
            throw new AdminException("존제하지 않는 강의 입니다");
        }
        LectureApplyEntity lectureApplyEntity = optionalLectureApplyEntity.get();

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
                .score(lectureApplyEntity.getLectureNameEntity().getScore())
                .lectureStrDate(lectureApplyEntity.getSemesterEntity().getSemesterStrDate())
                .lectureEndDate(lectureApplyEntity.getSemesterEntity().getSemesterEndDate())
                .lectureStrTime(passStrTime)
                .lectureEndTime(passEndTime)
                .gradeLimit(lectureApplyEntity.getGradeLimit())
                .attendance(lectureApplyEntity.getAttendance())
                .midtermExamination(lectureApplyEntity.getMidtermExamination())
                .finalExamination(lectureApplyEntity.getFinalExamination())
                .textBook(lectureApplyEntity.getBookUrl())
                .ctnt(lectureApplyEntity.getCtnt())
                .bookUrl(lectureApplyEntity.getBookUrl())
                .build();


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
        if (dto.getProcedures()==2){
            LocalDate lectureApplyDeadline = applyEntity.getSemesterEntity().getLectureApplyDeadline();
            LocalDate applyDeadline = lectureApplyDeadline.plusWeeks(2);
            applyEntity.setStudentsApplyDeadline(applyDeadline);
        }

        APPLY_RPS.save(applyEntity);

        return AdminUpdLectureRes.builder().ilecture(applyEntity.getIlecture()).ctnt(applyEntity.getCtnt()).procedures(applyEntity.getOpeningProceudres()).build();
    }

    public void excelTest(HttpServletResponse response, Long studentNum) throws IOException {
//엑셀 생성
//        Workbook workbook = new HSSFWorkbook();
//        int rowIdx= 0;
//        Sheet sheet = workbook.createSheet("학생 테스트");
//
//        Row row = sheet.createRow(rowIdx++);
//        row.createCell(0).setCellValue("학번");
//        row.createCell(1).setCellValue("이름");
//        row.createCell(2).setCellValue("전공");
//        row.createCell(3).setCellValue("학년");
//
//        collegeStudent[] list = {
//                new collegeStudent(2022032401L,"김학생",2,"M","경영학"),
//                new collegeStudent(2022032402L,"이학생",1,"F","컴퓨터공학"),
//                new collegeStudent(2022032403L,"박학생",2,"M","통계학"),
//                new collegeStudent(2022032404L,"최학생",1,"F","식품영양학"),
//                new collegeStudent(2022032405L,"정학생",1,"M","영화예술학"),
//                new collegeStudent(2022032406L,"강학생",4,"M","디자인학"),
//                new collegeStudent(2022032407L,"조학생",1,"F","심리학"),
//                new collegeStudent(2022032408L,"윤학생",1,"F","경제학"),
//                new collegeStudent(2022032409L,"장학생",3,"M","체육학"),
//                new collegeStudent(2022032410L,"임학생",2,"M","화학"),
//                new collegeStudent(2022032411L,"한학생",1,"M","기계공학"),
//                new collegeStudent(2022032412L,"오학생",4,"F","의예"),
//                new collegeStudent(2022032413L,"서학생",1,"F","조경학"),
//                new collegeStudent(2022032414L,"신학생",1,"M","수학"),
//                new collegeStudent(2022032415L,"권학생",3,"M","물리"),
//                new collegeStudent(2022032416L,"황학생",2,"M","미술"),
//                new collegeStudent(2022032417L,"안학생",3,"M","제약"),
//                new collegeStudent(2022032418L,"송학생",4,"F","철학"),
//                new collegeStudent(2022032419L,"전학생",2,"M","의상학"),
//                new collegeStudent(2022032420L,"홍학생",1,"M","반도체")
//        };

        List<StudentEntity> all = STUDENT_RPS.findAll();
        int size = all.size();
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("학생부 명단");
        int rowNo = 0;

        Row headerRow = sheet.createRow(rowNo++);
        headerRow.createCell(0).setCellValue("학번");
        headerRow.createCell(1).setCellValue("이름");
        headerRow.createCell(2).setCellValue("학년");
        headerRow.createCell(3).setCellValue("성별");
        headerRow.createCell(4).setCellValue("전공");




        for (StudentEntity s : all) {
            Row row = sheet.createRow(rowNo++);
            row.createCell(0).setCellValue(s.getStudentNum());
            row.createCell(1).setCellValue(s.getNm());
            row.createCell(2).setCellValue(s.getGrade());
            row.createCell(3).setCellValue(s.getGender().toString());
            row.createCell(4).setCellValue(s.getMajorEntity().getMajorName());
        }
        String format = String.format("attachment;filename=%s학생명단.xls", LocalDate.now());
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", format);

        workbook.write(response.getOutputStream());
        workbook.close();

    }

}
