package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.lecturecondition.LectureConditionRepository;
import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.admin.model.*;
import com.green.smartgradever2.admin.professor.AdminProfessorRepository;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.admin.student.AdminStudentRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.config.entity.model.GenderEnum;
import com.green.smartgradever2.config.exception.AdminException;

import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.student.model.StudentHistoryVo;
import com.green.smartgradever2.student.model.StudentScheduleRes;
import com.green.smartgradever2.student.model.StudentScheduleVo;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
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
    private final AdminProfessorRepository PRO_RPS;


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


    public void greenUniversityMember(HttpServletResponse response) throws IOException {


// 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();

        // 시트 생성
        Sheet sheet = workbook.createSheet("전교 학생 리스트");


        // 제목 행 생성
        Row headerRow = sheet.createRow(0);

        // 제목 행 스타일 설정
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);


        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i))+512);
        }


        String[] headers = { "학번", "이름", "학년", "성별", "학과" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // 데이터 행 생성 및 셀 스타일 설정
        CellStyle cellCellStyle = workbook.createCellStyle();
        Font cellFont = workbook.createFont();

        cellFont.setFontName("맑은 고딕");
        cellFont.setBold(false);
        cellFont.setFontHeight((short) 250);
        cellFont.setFontHeightInPoints((short) 14);
        cellCellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellCellStyle.setFont(cellFont);
        cellCellStyle.setBorderTop(BorderStyle.THIN);
        cellCellStyle.setBorderLeft(BorderStyle.THIN);
        cellCellStyle.setBorderRight(BorderStyle.THIN);



        CellStyle numericCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        numericCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0")); // 숫자 형식 지정
        int rowCount = 1; // 첫 번째 행은 제목 행이므로 1부터 시작

        List<StudentEntity> studentEntities = STUDENT_RPS.findAll();

        int strIdx=0;
        int endIdx=0;
        for (int i = 0; i < studentEntities.size(); i++) {
            Row row = sheet.createRow(rowCount++);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(studentEntities.get(i).getStudentNum());
            cell1.setCellStyle(cellCellStyle);

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(studentEntities.get(i).getNm());
            cell2.setCellStyle(cellCellStyle);

            Cell cell3 = row.createCell(2);
            cell3.setCellValue(studentEntities.get(i).getGrade());
            cell3.setCellStyle(cellCellStyle);

            Cell cell4 = row.createCell(3);
            cell4.setCellValue(studentEntities.get(i).getGender().toString());
            cell4.setCellStyle(numericCellStyle);
            cell4.setCellStyle(cellCellStyle);

            Cell cell5 = row.createCell(4);
            cell5.setCellValue(studentEntities.get(i).getMajorEntity().getMajorName());
            cellCellStyle.setBorderBottom(BorderStyle.THIN);
            cell5.setCellStyle(cellCellStyle);

            if (i==0){
                strIdx=row.getRowNum();
                log.info("strIdx : {}",strIdx);
            }
            if (i==studentEntities.size()-1){
                endIdx=row.getLastCellNum();
                log.info("endIdx : {}",endIdx);
            }
            //학년 cell에 정렬 함수 설정
        }
        sheet.setAutoFilter(new CellRangeAddress(0, rowCount-1, 0, headers.length - 1));
        //   sheet.setAutoFilter(new CellRangeAddress(headerRow.getRowNum(), rowCount - 1, 4, 4));


        // 열 너비 자동 조정
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 시트 생성
        Sheet sheet1 = workbook.createSheet("전교 교수 리스트");
        for (int i = 0; i < sheet1.getLastRowNum(); i++) {
            sheet1.autoSizeColumn(i);
            sheet1.setColumnWidth(i, (sheet.getColumnWidth(i))+512);
        }

        // 제목 행 생성
        Row headerRow1 = sheet1.createRow(0);

        // 제목 행 스타일 설정

        String[] headers1 = { "교수번호", "이름", "  근속년수  ", "성별", "학과" };

        for (int i = 0; i < headers1.length; i++) {
            Cell cell = headerRow1.createCell(i);
            cell.setCellValue(headers1[i]);
            cell.setCellStyle(headerCellStyle);
        }


        List<ProfessorEntity> professorEntityList = PRO_RPS.findAll();

        rowCount = 1;
        for (int i = 0; i < professorEntityList.size(); i++) {
            Row row = sheet1.createRow(rowCount++);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(professorEntityList.get(i).getIprofessor());
            cell1.setCellStyle(cellCellStyle);

            Cell cell2 = row.createCell(1);
            cell2.setCellValue(professorEntityList.get(i).getNm());
            cell2.setCellStyle(cellCellStyle);

            Cell cell3 = row.createCell(2);
            cell3.setCellValue(getValue(professorEntityList, i));
            cell3.setCellStyle(cellCellStyle);

            Cell cell4 = row.createCell(3);
            cell4.setCellValue(professorEntityList.get(i).getGender().toString());
            cell4.setCellStyle(numericCellStyle);
            cell4.setCellStyle(cellCellStyle);

            Cell cell5 = row.createCell(4);
            cell5.setCellValue(professorEntityList.get(i).getMajorEntity().getMajorName());
            cellCellStyle.setBorderBottom(BorderStyle.THIN);
            cell5.setCellStyle(cellCellStyle);

            if (i==0){
                strIdx=row.getRowNum();
                log.info("strIdx : {}",strIdx);
            }
            if (i==studentEntities.size()-1){
                endIdx=row.getLastCellNum();
                log.info("endIdx : {}",endIdx);
            }
            //학년 cell에 정렬 함수 설정
        }
        sheet1.setAutoFilter(new CellRangeAddress(0, rowCount-1, 0, headers.length - 1));



        // 열 너비 자동 조정
        for (int i = 0; i < headers.length; i++) {
            sheet1.autoSizeColumn(i);
        }

        String fileName = "GreenUniversityMember.xlsx"; // 원하는 파일 이름을 지정합니다.
        String format = String.format("attachment;filename=%s_%s",LocalDate.now().toString(), fileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", format);


        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private static int getValue(List<ProfessorEntity> professorEntityList, int i) {
        int year = LocalDate.now().getYear() - professorEntityList.get(i).getCreatedAt().getYear();

        if (year==0){
            return 1;
        }
        return year;
    }


    /**최근 4년간 입학생 성비**/
    public AdminStudentYearGenderRes findStudentYearGender(){
        List<AdminStudentYearGenderCountVo> female = MAPPER.genderCount(GenderEnum.F.toString());
        List<AdminStudentYearGenderCountVo> man = MAPPER.genderCount(GenderEnum.M.toString());

        return AdminStudentYearGenderRes.builder().man(man).female(female).build();
    }




}
