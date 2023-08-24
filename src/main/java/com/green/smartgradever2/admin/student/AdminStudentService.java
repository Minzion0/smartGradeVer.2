package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.admin.student.model.*;
import com.green.smartgradever2.entity.MajorEntity;
import com.green.smartgradever2.entity.StudentEntity;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminStudentService {

    private final AdminStudentRepository RPS;
    private final AdminMajorRepository MAJOR_RPS;
    private final PasswordEncoder PW_ENCODER;
    private final EntityManager EM;
    private final AdminStudentMapper MAPPER;


    //@Transactional(rollbackFor = Exception.class)
    public AdminInsStudentVo insStudent(AdminInsStudentParam param) {

        Optional<MajorEntity> major = MAJOR_RPS.findById(param.getImajor());

        log.info("major : {}", major);

        LocalDate now = LocalDate.now();
        LocalDate setYear = LocalDate.of(now.getYear(), 1, 1);
        LocalDateTime startOfDay = setYear.atStartOfDay();
        LocalDateTime endOfDay = setYear.plusYears(1).atStartOfDay().minusNanos(1);
        String year = setYear.toString().substring(2, 4);

        List<StudentEntity> majorCount = RPS.findByMajorEntityAndCreatedAtBetween(major.get(), startOfDay, endOfDay);

        String password = param.getBirthdate().toString().replaceAll("-", "");


        String num = String.format("%s%02d%04d", year, major.get().getImajor(), majorCount.size() + 1);
        int studentNum = Integer.parseInt(num);

        String encode = PW_ENCODER.encode(password);

        StudentEntity entity = new StudentEntity();
        entity.setStudentNum(studentNum);
        entity.setStudentPassword(encode);
        entity.setNm(param.getNm());
        entity.setGender(param.getGender());
        entity.setMajorEntity(major.get());
        entity.setBirthdate(param.getBirthdate());
        entity.setPhone(param.getPhone());


        StudentEntity save = RPS.save(entity);

        EM.detach(save);

        Optional<StudentEntity> result = RPS.findById(save.getStudentNum());
        StudentEntity student = result.get();


        return AdminInsStudentVo.builder().studentNum(student.getStudentNum())
                                            .nm(student.getNm())
                                            .grade(student.getGrade())
                                            .birthdate(student.getBirthdate())
                                            .delYn(student.getDelYn())
                                            .finishedYn(student.getFinishedYn())
                                            .phone(student.getPhone())
                                            .gender(student.getGender())
                                            .imajor(student.getMajorEntity().getImajor())
                                            .createdAt(student.getCreatedAt())
                                            .build();

    }


    public AdminStudentRes findStudents(AdminStudentFindParam param,Pageable pageable){

        pageable.getPageSize();
        pageable.getOffset();
        pageable.getPageNumber();

        AdminStudentFindDto dto = new AdminStudentFindDto();
        dto.setImajor(param.getImajor());
        dto.setGrade(param.getGrade());
        dto.setFinishedYn(param.getFinishedYn());
        dto.setStudentNum(param.getStudentNum());
        dto.setNm(param.getNm());


        int page = MAPPER.countStudents(dto);
        PagingUtils utils = new PagingUtils(pageable.getPageNumber(),page);

        dto.setStaIdx(utils.getStaIdx());
        dto.setRow(utils.getROW());

        List<AdminStudentFindVo> students = MAPPER.findStudents(dto);

        AdminStudentRes res = new AdminStudentRes();
        res.setPage(utils);
        res.setStudents(students);

        return res;
    }


}
