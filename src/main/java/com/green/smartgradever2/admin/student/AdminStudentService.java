package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.admin.student.model.AdminInsStudentParam;
import com.green.smartgradever2.admin.student.model.AdminInsStudentVo;
import com.green.smartgradever2.entity.MajorEntity;
import com.green.smartgradever2.entity.StudentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Transactional(rollbackFor = Exception.class)
    public AdminInsStudentVo insStudent(AdminInsStudentParam param){

        Optional<MajorEntity> major = MAJOR_RPS.findById(param.getImajor());

        log.info("major : {}",major);

//        LocalDate now = LocalDate.now();
        LocalDate now = LocalDate.of(2023, 1, 1);
        LocalDateTime startOfDay = now.atStartOfDay();
        LocalDateTime endOfDay = now.plusYears(1).atStartOfDay().minusNanos(1);
        String year = now.toString().substring(2, 4);

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


        Optional<StudentEntity> result = RPS.findById(save.getStudentNum());
        StudentEntity student = result.get();
     return    AdminInsStudentVo.builder().studentNum(save.getStudentNum())
                                    .nm(save.getNm())
                                    .grade(save.getGrade())
                                    .birthdate(save.getBirthdate())
                                    .delYn(save.getDelYn())
                                    .finishedYn(save.getFinishedYn())
                                    .phone(save.getPhone())
                                    .gender(save.getGender())
                                    .imajor(save.getMajorEntity().getImajor())
                                    .createdAt(save.getCreatedAt())
                                    .build();


    }
//
//    public AdminStudentVo(AdminInsStudentDto dto) {
//        this.istudent=dto.getIstudent();
//        this.studentNum=dto.getStudentNum();
//        this.imajor = dto.getImajor();
//        this.nm = dto.getNm();
//        this.gender = dto.getGender();
//        this.birthdate = dto.getBirthdate();
//        this.phone = dto.getPhone();
//        this.finishedYn = 1;
//        this.createdAt = LocalDateTime.now();
//        this.grade = 1;
//        this.delYn=0;
//    }



}
