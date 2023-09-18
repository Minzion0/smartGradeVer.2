package com.green.smartgradever2.student;

import com.green.smartgradever2.admin.professor.model.AdminProfessorLectureVo;
import com.green.smartgradever2.admin.semester.SemesterQdsl;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecture_apply.model.LectureApplySelDto;
import com.green.smartgradever2.lecture_apply.model.LectureSelAllRes;
import com.green.smartgradever2.lectureschedule.LectureScheduleRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.professor.ProfessorRepository;
import com.green.smartgradever2.student.model.*;
import com.green.smartgradever2.utils.FileUtils;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentMapper MAPPER;
    private final StudentRepository studentRep;
    private final LectureApplyRepository lectureApplyRep;
    private final LectureStudentRepository lectureStudentRep;
    private final LectureScheduleRepository lectureScheduleRep;
    private final PasswordEncoder PW_ENCODER;
    private final ProfessorRepository professorRepository;
    private final StudentQdsl qdsl;
    private final SemesterRepository semesterRepository;
    private final SemesterQdsl semesterQdsl;


    @Value("${file.dir}")
    private String fileDir;

    @Transactional
    public StudentUpRes upStudent(MultipartFile pic, StudentParam param, Long StudentNum) {
        StudentEntity student = studentRep.findBystudentNum(StudentNum);

        if (student == null) {
            return null;
        }
        student.setAddress(param.getAddress());
        student.setEmail(param.getEmail());
        student.setPhone(param.getPhone());

        if (pic != null) {
            String centerPath = String.format("students/%d", student.getStudentNum());
            String dicPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), centerPath);
            String savedFileName = FileUtils.makeRandomFileNm(pic.getOriginalFilename());
            String savedFilePath = String.format("%s/%s", centerPath, savedFileName);
            String targetPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), savedFilePath);


            File dic = new File(dicPath);
            if (!dic.exists()) {
                dic.mkdirs();
            }

            File target = new File(targetPath);
            try {
                pic.transferTo(target);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패");
            }

            student.setPic(savedFileName);
        }
        try {
            studentRep.save(student);
        } catch (Exception e) {
            // 업로드에 실패한 경우 파일 삭제
            if (pic != null) {
                String picFileName = student.getPic();
                String picPath = String.format("students/%d/%s", student.getStudentNum(), picFileName);
                String fullPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), picPath);

                File picFile = new File(fullPath);
                if (picFile.exists()) {
                    picFile.delete();
                }
            }
            throw new RuntimeException("사진 불가");
        }


        return new StudentUpRes(student);
    }


    @Transactional
    public void studentDelPic(Long studentNum) {
        StudentEntity studnet = studentRep.findBystudentNum(studentNum);

        if (studnet != null && studnet.getPic() != null) {
            //기존 사진이 있으면 삭제
            String picFileName = studnet.getPic();
            String picPath = String.format("students/%d/%s", studentNum, picFileName);
            String fullPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), picPath);

            File picFile = new File(fullPath);
            if (picFile.exists()) {
                picFile.delete();
            }

            studnet.setPic(null);

            try {
                studentRep.save(studnet);
            } catch (Exception e) {
                throw new RuntimeException("사진 삭제 실패");
            }


        }


    }


    //수강신청
    public StudentRegisterRes registerLectureForStudent(Long ilecture, Long studentNum) {
        StudentRegisterRes response = new StudentRegisterRes();
        StudentEntity student = studentRep.findBystudentNum(studentNum);
        // 학생이 수강하려는 강의 정보 가져오기
        LectureApplyEntity lectureApply = lectureApplyRep.findById(ilecture)
                .orElseThrow(() -> new EntityNotFoundException("강의를 찾을수 없습니다."));

        // 해당 강의의 수강 인원 확인
        if (lectureApply.getLectureMaxPeople() <= lectureStudentRep.countByLectureApplyEntity(lectureApply)) {
            response.setSuccess(false);
            response.setMessage("강의 등록이 가득찼습니다.");
            return response;
        }

        if (lectureStudentRep.existsByStudentEntityAndLectureApplyEntity(student, lectureApply)) {
            response.setSuccess(false);
            response.setMessage("이미 신청한 강의입니다.");
            return response;
        }

        if (lectureApply.getOpeningProceudres() != 2 || LocalDate.now().isAfter(lectureApply.getStudentsApplyDeadline())) {
            response.setSuccess(false);
            response.setMessage("신청 기간이 종료되었거나 강의 신청이 불가능한 강의입니다.");
            return response;
        }


        // 학생과 강의 정보를 수강 신청 테이블에 등록
        LectureStudentEntity lectureStudent = LectureStudentEntity.builder()
                .studentEntity(studentRep.findById(studentNum)
                        .orElseThrow(() -> new EntityNotFoundException("학생을 찾을수 없습니다.")))
                .lectureApplyEntity(lectureApply)
                .build();
        lectureStudentRep.save(lectureStudent);


        //수강 신청한 강의 시작시간 ,종료시간 , 시작요일
        LectureScheduleEntity lectureSchedule = lectureScheduleRep.findByIlecture(ilecture);
        if (lectureSchedule != null) {
            response.setDayWeek(lectureSchedule.getDayWeek());
            response.setLectureStrTime(lectureSchedule.getLectureStrTime());
            response.setLectureEndTime(lectureSchedule.getLectureEndTime());
        }

        response.setSuccess(true);
        response.setMessage("수강 신청 성공.");
        response.setLectureName(lectureApply.getLectureNameEntity().getLectureName()); // 강의 이름 설정
        response.setIlectureStudent(lectureStudent.getIlectureStudent());
        response.setIlecture(lectureApply.getIlecture());
        response.setStudentNum(Long.valueOf(lectureStudent.getStudentEntity().getStudentNum()));
        response.setAttendance(lectureStudent.getAttendance());
        response.setMidtermExamination(lectureStudent.getMidtermExamination());
        response.setFinalExamination(lectureStudent.getFinalExamination());
        response.setTotalScore(lectureStudent.getTotalScore());
        response.setFinishedAt(lectureStudent.getFinishedAt());
        response.setCorrectionAt(lectureStudent.getCorrectionAt());
        response.setFinishedYn(lectureStudent.getFinishedYn());
        response.setDelYn(lectureStudent.getDelYn());
        response.setObjection(lectureStudent.getObjection());
        // 필요한 정보를 저장하는 로직 추가

        return response;
    }

    //학생프로필 디데일
    public StudentFileSelRes getStudentProfileWithLectures(Long studentNum) {
        StudentEntity student = studentRep.findById(studentNum).get();
        if (student == null) {
            log.error("학생이 없습니다.: {}", studentNum);
            return null;
        }
        StudentProfileVo profile = new StudentProfileVo();
        profile.setStudentNum(student.getStudentNum());
        profile.setName(student.getNm());
        profile.setMajorName(student.getMajorEntity().getMajorName());
        profile.setGrade(student.getGrade());
        profile.setGender(student.getGender());
        profile.setBirthdate(student.getBirthdate());
        profile.setAddress(student.getAddress());
        profile.setPhone(student.getPhone());
        profile.setEmail(student.getEmail());
        profile.setGender(student.getGender());
        profile.setPic(student.getPic());
        profile.setFinishedYn(student.getFinishedYn());

        profile.setSecretKey(student.getSecretKey() == null ? "false" : "true");

        List<LectureStudentEntity> lectureApplyEntityList = lectureStudentRep.findByStudentEntity(student);


        List<StudentProfileLectureVo> lectureList = lectureApplyEntityList.stream().filter(lecture -> lecture.getFinishedYn() == 0).filter(lecture->lecture.getLectureApplyEntity().getOpeningProceudres() == 3).map(lecture -> {
            LectureApplyEntity lectureApplyEntity = lecture.getLectureApplyEntity();
            LectureScheduleEntity lectureScheduleEntity = lectureApplyEntity.getLectureScheduleEntity();

            LectureNameEntity lectureNameEntity = lectureApplyEntity.getLectureNameEntity();
            int lectureScore = lectureNameEntity.getScore(); // 강의 학점

            StudentProfileLectureVo lectureVo = StudentProfileLectureVo.builder()
                    .ilecture(lectureApplyEntity.getIlecture())
                    .lectureStrTime(lectureScheduleEntity.getLectureStrTime())
                    .lectureEndTime(lectureScheduleEntity.getLectureEndTime())
                    .lectureStrDate(lectureApplyEntity.getSemesterEntity().getSemesterStrDate())
                    .lectureEndDate(lectureApplyEntity.getSemesterEntity().getSemesterEndDate())
                    .lectureName(lectureNameEntity.getLectureName())
                    .dayWeek(lectureScheduleEntity.getDayWeek())
                    .score(lectureScore) // 강의 학점 설정
                    .build();

            return lectureVo;
        }).toList();

        int totalScore = lectureApplyEntityList.stream()
                .filter(lecture -> lecture.getFinishedYn() == 1) //
                .mapToInt(lecture -> lecture.getLectureApplyEntity().getLectureNameEntity().getScore())
                .sum();

        // 총 학점을 프로필에 설정
        profile.setScore(totalScore);

        LocalDate localDate= null;
        if (!lectureApplyEntityList.isEmpty()){
            LocalDate lectureApplyDeadline = lectureApplyEntityList.get(0).getLectureApplyEntity().getSemesterEntity().getLectureApplyDeadline();

            localDate = lectureApplyDeadline.plusWeeks(1);
        }

        if (lectureApplyEntityList.isEmpty()){
            SemesterEntity semester = semesterQdsl.findSemester();

            localDate= semester.getLectureApplyDeadline().plusWeeks(1);
        }



        StudentFileSelRes result = StudentFileSelRes.builder()
                .profile(profile)
                .deadline(localDate.toString())
                .lectureList(lectureList)
                .build();

        return result;


    }


    // 학생 강의별 성적
    public List<StudentSelVo> getStudentLectureGrades(StudentEntity studentEntity, Pageable page) {
        List<LectureStudentEntity> lectureGrades = lectureStudentRep.findByStudentEntity(studentEntity);
        List<StudentSelVo> studentSelVos = new ArrayList<>(); // 각 강의별 성적 정보를 담을 리스트

        for (LectureStudentEntity lectureGrade : lectureGrades) {
            StudentSelVo vo = new StudentSelVo();

            // 강의별 성적 정보를 각각의 필드에 설정
            vo.setStudentNum(studentEntity.getStudentNum());
            vo.setIlectureStudent(lectureGrade.getIlectureStudent());
            vo.setIlecture(lectureGrade.getLectureApplyEntity().getIlecture());
            vo.setStudentGrade(lectureGrade.getStudentEntity().getGrade());
            vo.setIsemester(lectureGrade.getLectureApplyEntity().getSemesterEntity().getIsemester());
            vo.setYear(lectureGrade.getLectureApplyEntity().getSemesterEntity().getYear());
            vo.setProfessorName(lectureGrade.getLectureApplyEntity().getProfessorEntity().getNm());
            vo.setLectureName(lectureGrade.getLectureApplyEntity().getLectureNameEntity().getLectureName());
            vo.setDayWeek(lectureGrade.getLectureApplyEntity().getLectureScheduleEntity().getDayWeek());
            vo.setLectureStrTime(lectureGrade.getLectureApplyEntity().getLectureScheduleEntity().getLectureStrTime());
            vo.setLectureEndTime(lectureGrade.getLectureApplyEntity().getLectureScheduleEntity().getLectureEndTime());
            vo.setScore(lectureGrade.getLectureApplyEntity().getLectureNameEntity().getScore());
            vo.setObjection(lectureGrade.getObjection());

            vo.setFinishedYn(lectureGrade.getFinishedYn());
            vo.setAttendance(lectureGrade.getAttendance());
            vo.setMidtermExamination(lectureGrade.getMidtermExamination());
            vo.setFinalExamination(lectureGrade.getFinalExamination());
            vo.setTotalScore(lectureGrade.getTotalScore());


            GradeUtils gradeUtils = new GradeUtils();
            String grade = gradeUtils.totalGradeFromScore1(lectureGrade.getTotalScore());
            vo.setGrade(grade);

            // 학점 계산 및 설정
            double score = gradeUtils.totalScore();
            String rating = gradeUtils.totalGradeFromScore(lectureGrade.getTotalScore());
            vo.setRating(rating);

            studentSelVos.add(vo); // 각각의 강의별 성적 정보를 리스트에 추가
        }

        return studentSelVos;  // 강의별 성적 정보 리스트 반환
    }

    public StudentEntity getStudentById(Long studentNum) {

        return studentRep.findBystudentNum(studentNum);
    }


    // 학생 학점 조회
    public StudentInfoDto getStudentInfo(Long studentNum) {
        StudentEntity student = studentRep.findBystudentNum(studentNum);
        int selfStudyCredit = calculateSelfStudyCredit(studentNum);
        String majorName = student.getMajorEntity().getMajorName();
        int graduationScore = student.getMajorEntity().getGraduationScore();
        int remainingPoints = graduationScore - selfStudyCredit;

        StudentInfoDto studentInfoDTO = new StudentInfoDto();
        studentInfoDTO.setStudentNum(studentNum);
        studentInfoDTO.setSelfStudyCredit(selfStudyCredit);
        studentInfoDTO.setGraduationScore(graduationScore);
        studentInfoDTO.setRemainingPoints(remainingPoints);
        studentInfoDTO.setMajorName(majorName);

        return studentInfoDTO;
    }

    public int calculateSelfStudyCredit(Long studentNum) {
        List<LectureStudentEntity> finishedLectures = lectureStudentRep.findByStudentEntityStudentNumAndFinishedYn(studentNum, 1);
        int totalCredit = 0;

        for (LectureStudentEntity lectureStudent : finishedLectures) {
            totalCredit += lectureStudent.getLectureApplyEntity().getLectureNameEntity().getScore();
        }

        return totalCredit;
    }

    /**
     * 학생 비밀번호 변경 (로그인 완료 시 가능)
     **/
    public String updPassword(StudentPasswordParam param, StudentUpdPasswordDto dto) throws Exception {
        StudentEntity entity = studentRep.findById(dto.getIstudent()).get();

        if (!PW_ENCODER.matches(param.getCurrentPassword(), entity.getStudentPassword())) {
            throw new Exception("기존 비밀번호를 다시 확인해주세요");
        }
        String npw = PW_ENCODER.encode(param.getStudentPassword());
        entity.setStudentPassword(npw);
        studentRep.save(entity);

        return "비밀번호 변경이 완료되었습니다.";
    }

    public void updateObjection(Long studentNum, Long ilectureStudent, StudentObjectionDto objectionDto) {
        LectureStudentEntity lectureStudentEntity = lectureStudentRep.findByStudentEntityStudentNumAndIlectureStudent(studentNum, ilectureStudent);
        if (lectureStudentEntity != null) {

            LocalDate currentDateTime = LocalDate.now();
            LocalDate correctionAt = lectureStudentEntity.getCorrectionAt();

            if (correctionAt != null && currentDateTime.isAfter(correctionAt)) {
                throw new IllegalArgumentException("이의 신청 기간이 종료되었습니다.");
            }

            int objection = objectionDto.getObjection();

            if (objection >= 0 && objection <= 1) {
                lectureStudentEntity.setObjection(objection);
                lectureStudentRep.save(lectureStudentEntity);
            } else {
                throw new IllegalArgumentException("objection 값은 0에서 1 사이여야 합니다.");
            }
        } else {
            throw new IllegalArgumentException("학생과 강의 학생이 일치하지 않습니다.");
        }


//        if (lectureStudentEntity != null) {
//            lectureStudentEntity.setObjection(objectionDto.getObjection());
//            lectureStudentRep.save(lectureStudentEntity);
//        } else {
//            try {
//                throw new Exception("학생과 강의 학생이 일치하지 않습니다.");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    public StudentHistoryRes studentHistoryRes(StudentHistoryOpenDto dto, Pageable pageable) {
        StudentEntity entity = studentRep.findById(dto.getStudentNum()).get();
        Page<LectureStudentEntity> studentpage = lectureStudentRep.findByStudentEntity(entity, pageable);

        List<StudentHistoryDto> studentList = studentpage.getContent().stream().filter(student -> {
                    // 교수 이름 필터링: nm 파라미터와 교수 이름이 일치하거나 nm이 비어 있으면 모두 포함
                    String professorName = student.getLectureApplyEntity().getProfessorEntity().getNm();
                    return dto.getNm() == null || professorName.equals(dto.getNm());
                })
                .filter(student -> {
                    // 강의 명 필터링: lectureName 파라미터와 강의 명이 일치하거나 lectureName이 비어 있으면 모두 포함
                    String lectureName = student.getLectureApplyEntity().getLectureNameEntity().getLectureName();
                    return dto.getLectureName() == null || lectureName.equals(dto.getLectureName());
                }).filter(student -> {
                    // Year filtering: year parameter and lecture year should match or year is not provided
                    int lectureYear = student.getLectureApplyEntity().getSemesterEntity().getYear();
                    return dto.getYear() == 0 || lectureYear == dto.getYear();
                }).filter(student -> student.getLectureApplyEntity().getOpeningProceudres() == 3|| student.getLectureApplyEntity().getOpeningProceudres() ==4 ).map(student -> {
            StudentHistoryDto studentHistoryDto = new StudentHistoryDto();
            studentHistoryDto.setIsemester(student.getLectureApplyEntity().getSemesterEntity().getIsemester());
            studentHistoryDto.setYear(student.getLectureApplyEntity().getLectureScheduleEntity().getSemesterEntity().getYear());
            studentHistoryDto.setScore(student.getLectureApplyEntity().getLectureNameEntity().getScore());
            studentHistoryDto.setLectureName(student.getLectureApplyEntity().getLectureNameEntity().getLectureName());
            studentHistoryDto.setProfessorName(student.getLectureApplyEntity().getProfessorEntity().getNm());
            studentHistoryDto.setDayWeek(student.getLectureApplyEntity().getLectureScheduleEntity().getDayWeek());
            studentHistoryDto.setLectureStrTime(student.getLectureApplyEntity().getLectureScheduleEntity().getLectureStrTime());
            studentHistoryDto.setLectureEndTime(student.getLectureApplyEntity().getLectureScheduleEntity().getLectureEndTime());
            studentHistoryDto.setFinishedYn(student.getFinishedYn());
            studentHistoryDto.setGrade(student.getStudentEntity().getGrade());
            studentHistoryDto.setTextbook(student.getLectureApplyEntity().getTextbook());
            studentHistoryDto.setCtnt(student.getLectureApplyEntity().getCtnt());
            studentHistoryDto.setBookUrl(student.getLectureApplyEntity().getBookUrl());
            return studentHistoryDto;
        }).toList();
//        long maxpage = studentRep.count();
//        PagingUtils utils = new PagingUtils(pageable.getPageNumber(), (int) maxpage, 10);
//
        long totalItems =studentpage.getTotalElements();

        // 페이지당 항목 수
        int pageSize = pageable.getPageSize();

        // 총 페이지 수 계산
        long maxPage = (totalItems + pageSize - 1) / pageSize;

        PagingUtils utils = new PagingUtils(pageable.getPageNumber(), (int)maxPage, pageable.getPageSize());

        return StudentHistoryRes.builder().page(utils).lectureList(studentList).build();
    }




    public StudentListLectrueRes getAllProfessorsLecturesWithFilters(StudentListLectureDto dto, Pageable pageable) {
        StudentEntity entity = studentRep.findBystudentNum(dto.getStudentNum());

        Page<StudentListLectureVo> studentListLectureVos = qdsl.selStudentLectureList(dto.getOpeningProcedures(),
                entity.getGrade(), pageable, entity.getStudentNum(), dto.getLectureName());
        log.info("ss : {}",studentListLectureVos);



//        long maxpage = lectureApplyRep.count();
        PagingUtils pagingUtils = new PagingUtils(dto.getPage(), (int)studentListLectureVos.getTotalElements());


        return StudentListLectrueRes.builder().lectureList(studentListLectureVos.stream().toList()).page(pagingUtils).build();
    }



    //학생 성적 출력
    public void studentGradePrint(Long studentNum, HttpServletResponse response) throws IOException {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentNum(studentNum);
        List<LectureStudentEntity> studentEntities = lectureStudentRep.findByStudentEntity(studentEntity);

        List<StudentHistoryVo> list = studentEntities.stream().filter(res-> res.getFinishedAt()!=null ).map(student -> StudentHistoryVo.builder()
                .year(student.getLectureApplyEntity().getSemesterEntity().getYear())
                .grade(student.getStudentEntity().getGrade())
                .semester(student.getLectureApplyEntity().getSemesterEntity().getSemester())
                .lectureName(student.getLectureApplyEntity().getLectureNameEntity().getLectureName())
                .professorName(student.getLectureApplyEntity().getProfessorEntity().getNm())
                .score(student.getTotalScore())
                .lectureScore(student.getLectureApplyEntity().getLectureNameEntity().getScore())
                .build()).toList();


        //파일 생성
        Workbook workbook = new XSSFWorkbook();


        // 시트 생성
        String format = String.format("%s 학생의 성적",studentEntities.get(0).getStudentEntity().getNm() );
        Sheet sheet = workbook.createSheet(format);


        Row headerRow = sheet.createRow(3);

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

        //시트 컬럼 여유공간 설정
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, (sheet.getColumnWidth(i))*2);
        }
        Row nameRow = sheet.createRow(0);

        String[] nameHeaders = {"학번","이름","학과"};
        for (int i = 0; i < nameHeaders.length; i++) {
            Cell cell = nameRow.createCell(i+2);
            cell.setCellValue(nameHeaders[i]);
            cell.setCellStyle(headerCellStyle);

        }


        // 제목 행 생성
        String[] headers = { "  학년     ","학기     ", "강의명    ", "교수명    ", "점수    ", "평점     ","평점     ","학점    " };

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
        int rowCount = 4; // 첫 번째 행은 제목 행이므로 1부터 시작


        Row nameSheetRow = sheet.createRow(1);
        Cell numRowCell = nameSheetRow.createCell(2);
        numRowCell.setCellStyle(cellCellStyle);
        numRowCell.setCellValue(studentEntities.get(0).getStudentEntity().getStudentNum());

        Cell nameRowCell = nameSheetRow.createCell(3);
        nameRowCell.setCellStyle(cellCellStyle);
        nameRowCell.setCellValue(studentEntities.get(0).getStudentEntity().getNm());

        Cell majorRowCell = nameSheetRow.createCell(4);
        majorRowCell.setCellStyle(cellCellStyle);
        majorRowCell.setCellValue(studentEntities.get(0).getStudentEntity().getMajorEntity().getMajorName());




        for (int i = 0; i < list.size(); i++) {
            GradeUtils gradeUtils = new GradeUtils(list.get(i).getScore());


            Row row = sheet.createRow(rowCount++);
            Cell cell = row.createCell(0);
            cell.setCellValue(list.get(i).getGrade());
            cell.setCellStyle(cellCellStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(list.get(i).getSemester());
            cell1.setCellStyle(cellCellStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(list.get(i).getLectureName());
            cell2.setCellStyle(cellCellStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(list.get(i).getProfessorName());
            cell3.setCellStyle(cellCellStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(list.get(i).getScore());
            cell4.setCellStyle(numericCellStyle);
            cell4.setCellStyle(cellCellStyle);


            Cell cell5 = row.createCell(5);
            cell5.setCellValue(gradeUtils.totalScore());
            cell5.setCellStyle(cellCellStyle);

            String rating = gradeUtils.totalStrRating(gradeUtils.totalScore());

            Cell cell6 = row.createCell(6);
            cell6.setCellValue(rating);
            cell6.setCellStyle(cellCellStyle);


            Cell cell7 = row.createCell(7);
            if ("F".equals(rating)){
                cell7.setCellValue(0);
            }else {

                cell7.setCellValue(list.get(i).getLectureScore());
            }
            cellCellStyle.setBorderBottom(BorderStyle.THIN);
            cell7.setCellStyle(cellCellStyle);



            //학년 cell에 정렬 함수 설정
        }
        CellRangeAddress region = new CellRangeAddress(rowCount, rowCount, 0, 6);
        int i1 = sheet.addMergedRegion(region);

        for (int row = region.getFirstRow(); row <= region.getLastRow(); row++) {
            Row r = sheet.getRow(row);
            if (r == null) {
                r = sheet.createRow(row);
            }

            for (int col = region.getFirstColumn(); col <= region.getLastColumn(); col++) {
                Cell cell = r.getCell(col);
                if (cell == null) {
                    cell = r.createCell(col);
                }
                cell.setCellStyle(headerCellStyle);
            }

            Cell cell = r.createCell(i1);
            cell.setCellValue("총 학점"); // 병합된 셀에 "총 학점" 값을 입력
            cell.setCellStyle(headerCellStyle);

            Cell sumFormulaCell = r.createCell(7); // 총 학점 값의 열에 SUM 함수 결과 입력
            sumFormulaCell.setCellFormula("SUM(H5:H" + rowCount + ")"); // A부터 G 열까지의 값의 SUM 함수 입력
            sumFormulaCell.setCellStyle(numericCellStyle);
            sumFormulaCell.setCellStyle(cellCellStyle);
        }


       //  // H열의 5행부터 현재 행까지의 수직 총합 계산


        sheet.setAutoFilter(new CellRangeAddress(3, rowCount-1, 0, headers.length - 1));
        //   sheet.setAutoFilter(new CellRangeAddress(headerRow.getRowNum(), rowCount - 1, 4, 4));


        // 열 너비 자동 조정
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }


        String formatted = String.format("GreenUniversity_%s.xlsx", studentNum);
        String formats = String.format("attachment;filename=%s_%s",LocalDate.now().toString(), formatted);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", formats);


        workbook.write(response.getOutputStream());
        workbook.close();


    }

    public List<StudentScheduleRes>studentSchedule(Long studentNum){
        List<StudentScheduleVo> studentSchedule = qdsl.findStudentSchedule(studentNum);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


        return    studentSchedule.stream().map(vo-> StudentScheduleRes.builder()
                .startTime(vo.getStartTime().format(formatter))
                .endTime(vo.getEndTime().format(formatter))
                .dayWeek(vo.getDayWeek())
                .lectureName(vo.getLectureName())
                .lectureRoomName(vo.getBuildingName()+" "+vo.getLectureRoomName())
                .build()).toList();

    }
    /**수강신청 철회**/
    public int lectureStudentDel(Long studentNum,Long ilecture){



        LectureApplyEntity lectureApplyEntity = new LectureApplyEntity();
        lectureApplyEntity.setIlecture(ilecture);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setStudentNum(studentNum);

        LectureStudentEntity entity = lectureStudentRep.findByLectureApplyEntityAndStudentEntity(lectureApplyEntity, studentEntity);

        try {
            lectureStudentRep.delete(entity);

        }catch (Exception e){
            return 0;//철회 실패
        }
        return  1;//철회 성공

    }

}




