package com.green.smartgradever2.admin.professor;

import com.green.smartgradever2.admin.professor.model.*;
import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.config.exception.AdminException;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.utils.CheckUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProfessorService {

    private final AdminProfessorRepository RPS;
    private final LectureApplyRepository LECTURE_RPS;
    private final AdminProfessorMapper MAPPER;
    private final PasswordEncoder PW_ENCODER;
    private final EntityManager EM;

    @Transactional
    public List<AdminProfessorInsVo> insProfessor(List<AdminProfessorInsParam> params) throws AdminException {
        List<ProfessorEntity> list = new ArrayList<>();
        ProfessorEntity professorEntity = null;
        for (AdminProfessorInsParam param : params) {
            MajorEntity major = new MajorEntity();
            major.setImajor(param.getImajor());

            CheckUtils utils = CheckUtils.builder().nm(param.getNm()).phoneNum(param.getPhone()).build();
            String msg = utils.getMsg();
            if (msg != null) {
                String msgs = String.format("%s 오류가 있습니다", msg);

                throw new AdminException(msgs);

            }


            Optional<List<ProfessorEntity>> orderByIprofessor = RPS.findAllByOrderByIprofessor();
            int size = 1;
            if (!orderByIprofessor.isEmpty()) {
                size = orderByIprofessor.get().size() + 1;
            }
            long iprofessor = 100000 + size;


            String setPw = param.getBirthdate().toString().replaceAll("-", "");

            ProfessorEntity professor = new ProfessorEntity();
            professor.setIprofessor(iprofessor);
            professor.setProfessorPassword(PW_ENCODER.encode(setPw));
            professor.setPhone(param.getPhone());
            professor.setNm(param.getNm());
            professor.setGender(param.getGender());
            professor.setBirthDate(param.getBirthdate());
            professor.setMajorEntity(major);
            try {

                professorEntity = RPS.saveAndFlush(professor);
            } catch (Exception e) {
                throw new AdminException(professor.getNm() + "등록시 오류 발생");
            }

            list.add(professorEntity);
        }


        EM.clear();

        List<AdminProfessorInsVo> vo = new ArrayList<>();
        for (ProfessorEntity professor : list) {
            ProfessorEntity entity = RPS.findById(professor.getIprofessor()).get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = entity.getCreatedAt().format(formatter);

            AdminProfessorInsVo build = AdminProfessorInsVo.builder().iprofessor(entity.getIprofessor())
                    .imajor(entity.getMajorEntity().getImajor())
                    .nm(entity.getNm())
                    .gender(entity.getGender())
                    .birthdate(entity.getBirthDate())
                    .phone(entity.getPhone())
                    .createdAt(format)
                    .delYn(entity.getDelYn())
                    .build();
            vo.add(build);
        }
        return vo;

    }


    public AdminProfessorFindRes findProfessors(Pageable pageable, AdminProfessorFindParam param) {
        Page<ProfessorEntity> list = null;

        if (param.getName() != null && param.getImajor() != 0) {
            MajorEntity major = new MajorEntity();
            major.setImajor(param.getImajor());
            list = RPS.findAllByNmAndMajorEntity(param.getName(), major, pageable);
        } else if (param.getName() != null) {
            list = RPS.findAllByNm(param.getName(), pageable);
        } else if (param.getImajor() != 0) {
            MajorEntity major = new MajorEntity();
            major.setImajor(param.getImajor());
            list = RPS.findAllByMajorEntity(major, pageable);
        } else {
            list = RPS.findAll(pageable);
        }

        PagingUtils pagingUtils = new PagingUtils(pageable.getPageNumber(), (int) list.getTotalElements());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<AdminProfessorFindVo> professors = list.getContent().stream().map(item -> AdminProfessorFindVo.builder()
                .iprofessor(item.getIprofessor())
                .majorName(item.getMajorEntity().getMajorName())
                .nm(item.getNm())
                .gender(item.getGender())
                .birthdate(item.getBirthDate())
                .phone(item.getPhone())
                .email(item.getEmail())
                .address(item.getAddress())
                .createdAt(item.getCreatedAt().format(formatter))
                .delYn(item.getDelYn()).build()).toList();

        return AdminProfessorFindRes.builder().professors(professors).page(pagingUtils).build();

    }

    public AdminProfessorDetailRes findProfessorDetail(Long iprofessor) {
        ProfessorEntity entity = RPS.findById(iprofessor).get();
        AdminProfessorProfileVo profile = new AdminProfessorProfileVo();
        profile.setIprofessor(entity.getIprofessor());
        profile.setName(entity.getNm());
        profile.setGender(entity.getGender());
        profile.setBirthdate(entity.getBirthDate());
        profile.setPhone(entity.getPhone());
        profile.setPic(entity.getPic());
        profile.setAddress(entity.getAddress());
        profile.setEmail(entity.getEmail());
        profile.setImajor(entity.getMajorEntity().getImajor());
        profile.setCreatedAt(entity.getCreatedAt());
        profile.setDelYn(entity.getDelYn());

        List<LectureApplyEntity> lectureEntityList = LECTURE_RPS.findByProfessorEntity(entity);

        List<AdminProfessorLectureVo> lectureList = lectureEntityList.stream().map(lecture -> AdminProfessorLectureVo.builder()
                .ilecture(lecture.getIlecture())
                .lectureStrTime(lecture.getLectureScheduleEntity().getLectureStrTime())
                .lectureEndTime(lecture.getLectureScheduleEntity().getLectureEndTime())
                .lectureStrDate(lecture.getSemesterEntity().getSemesterStrDate())
                .lectureEndDate(lecture.getSemesterEntity().getSemesterEndDate())
                .lectureName(lecture.getLectureNameEntity().getLectureName()).build()).toList();

        return AdminProfessorDetailRes.builder().lectureList(lectureList).profile(profile).build();

    }

    public AdminProfessorInsVo patchProfessor(Long iprofessor, AdminProfessorPatchParam param) throws Exception {

        Optional<ProfessorEntity> optionalProfessorEntity = RPS.findById(iprofessor);
        if (optionalProfessorEntity.isEmpty()) {
            throw new Exception("없는 회원 정보입니다");
        }
        ProfessorEntity professorEntity = optionalProfessorEntity.get();

        MajorEntity majorEntity = new MajorEntity();
        majorEntity.setImajor(param.getImajor());

        professorEntity.setNm(param.getName());
        professorEntity.setMajorEntity(majorEntity);

        RPS.save(professorEntity);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = professorEntity.getCreatedAt().format(formatter);

        return AdminProfessorInsVo.builder().iprofessor(professorEntity.getIprofessor())
                .imajor(professorEntity.getMajorEntity().getImajor())
                .nm(professorEntity.getNm())
                .gender(professorEntity.getGender())
                .birthdate(professorEntity.getBirthDate())
                .phone(professorEntity.getPhone())
                .createdAt(format)
                .delYn(professorEntity.getDelYn())
                .build();


    }

    public void professorListFile(HttpServletResponse response) throws IOException {


// 엑셀 워크북 생성
        Workbook workbook = new XSSFWorkbook();

        // 시트 생성


        // 제목 행 생성


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



        int strIdx=0;
        int endIdx=0;

        // 시트 생성
        Sheet sheet1 = workbook.createSheet("교수 리스트");
        for (int i = 0; i < sheet1.getLastRowNum(); i++) {
            sheet1.autoSizeColumn(i);
            sheet1.setColumnWidth(i, (sheet1.getColumnWidth(i))+512);
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


        List<ProfessorEntity> professorEntityList = RPS.findAll();

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


            //학년 cell에 정렬 함수 설정
        }
        sheet1.setAutoFilter(new CellRangeAddress(0, rowCount-1, 0, headers1.length - 1));



        // 열 너비 자동 조정
        for (int i = 0; i < headers1.length; i++) {
            sheet1.autoSizeColumn(i);
        }

        String fileName = "GreenUniversityProfessorList.xlsx"; // 원하는 파일 이름을 지정합니다.
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
}
