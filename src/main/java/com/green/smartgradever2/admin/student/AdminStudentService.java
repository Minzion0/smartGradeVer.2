package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.admin.student.model.*;
import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    @PersistenceContext
    private final EntityManager EM;
    private final AdminStudentMapper MAPPER;


    @Transactional
    public AdminInsStudentVo insStudent(AdminInsStudentParam param) {

        Optional<MajorEntity> major = MAJOR_RPS.findById(param.getImajor());

        log.info("major : {}", major);

        LocalDate now = LocalDate.now();
        LocalDate setYear = LocalDate.of(now.getYear(), 1, 1);
        LocalDateTime startOfDay = setYear.atStartOfDay();
        LocalDateTime endOfDay = setYear.plusYears(1).atStartOfDay().minusNanos(1);
        String year = setYear.toString().substring(2, 4);

        List<StudentEntity> majorCount = RPS.findAllByMajorEntityAndCreatedAtBetween(major.get(), startOfDay, endOfDay);

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


        RPS.saveAndFlush(entity);

        EM.clear();

        StudentEntity student = RPS.findById(entity.getStudentNum()).get();


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


        //jpa 좀더 학습후 컨버팅 실시
//        Page<StudentEntity> all = RPS.findAll(pageable);
//
//        all.stream().map(student->AdminStudentFindVo.builder()
//                .studentNum(student.getStudentNum())
//                .birthdate(student.getBirthdate())
//                .majorName(student.getMajorEntity().getMajorName())
//                .nm(student.getNm())
//                .phone(student.getPhone())
//                .gender(student.getGender())
//                .createdAt(student.getCreatedAt().toLocalDate())
//                .finishedYn(student.getFinishedYn())
//                .build()).toList();

        AdminStudentFindDto dto = new AdminStudentFindDto();
        dto.setImajor(param.getImajor());
        dto.setGrade(param.getGrade());
        dto.setFinishedYn(param.getFinishedYn());
        dto.setStudentNum(param.getStudentNum());

        if (param.getNm()!=null){
            // 이름에서 성만 검색한 경우
            if (param.getNm().length()==1){
                String format = String.format("^[%s]", param.getNm());
                dto.setNm(format);
            }
            if (param.getNm().length()==2){
                //성 빼고 이름만 검색한 경우
                String format = String.format("[_%s]$", param.getNm());
                dto.setNm(format);
            }
            if (param.getNm().length()>=3){
                String format = String.format("^[%s]", param.getNm());
                dto.setNm(format);
            }
        }


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

    public AdminStudentDetailRes studentDet(int studentNum){
        AdminStudentProfileVo profileVo = MAPPER.studentProfile(studentNum);
        List<AdminStudentLectureVo> lectureVos = MAPPER.studentLectures(studentNum);

        return AdminStudentDetailRes.builder().profile(profileVo).lectureList(lectureVos).build();
    }

    /**매년 말에 학생 진급 **/
    @Scheduled(cron = "0 59 23 31 12 ?")
    public void grade() {
        int result = MAPPER.promotionGrade();
        log.info("{}년도 진학한 학생의수 : {}", LocalDateTime.now().plusYears(1).getYear(), result);

    }

}
