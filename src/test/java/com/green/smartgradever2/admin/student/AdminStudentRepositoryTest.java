//package com.green.smartgradever2.admin.student;
//
//import com.green.smartgradever2.entity.MajorEntity;
//import com.green.smartgradever2.entity.StudentEntity;
//import com.green.smartgradever2.entity.model.GenderEnum;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@DataJpaTest
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Slf4j
//class AdminStudentRepositoryTest {
//    @Autowired
//    private AdminStudentRepository RPS;
//
//    @Test
//    void findByMajorEntity() {
//        MajorEntity major = new MajorEntity();
//        major.setImajor(10L);
//        LocalDate year = LocalDate.of(2023, 1, 1);
//        LocalDateTime startOfYear = year.atStartOfDay(); // 해당 년도의 첫째 날의 00:00:00
//        LocalDateTime endOfYear = year.plusYears(1).atStartOfDay().minusNanos(1); // 해당 년도의 마지막 날의 23:59:59.999999999
//      List<StudentEntity>byMajorEntity = RPS.findAllByMajorEntityAndCreatedAtBetween(major,startOfYear,endOfYear);
//        List<StudentEntity> all = RPS.findAll();
//
//        log.info("Entity : {}",byMajorEntity.size());
//        log.info("all : {}",all.size());
//
//        major.setImajor(20L);
//        List<StudentEntity> rps = RPS.findAllByMajorEntityAndCreatedAtBetween(major, startOfYear, endOfYear);
//
//        log.info("res : {}",rps.get(0).getMajorEntity().getImajor());
//        String substring = year.toString().substring(2,4);
//        if (major.getImajor()<10){
//
//            String format = String.format("%s%02d",substring , major.getImajor());
//            log.info("format : {}",format);
//        }
//        String format = String.format("%s%d%04d", substring, major.getImajor(), byMajorEntity.size() + 1);
//        int studentNum = Integer.parseInt(format);
//        log.info("student_num : {}",format);
//
//        StudentEntity entity = new StudentEntity();
//        entity.setStudentNum(studentNum);
//        entity.setMajorEntity(major);
//        entity.setNm("만득");
//        entity.setGender(GenderEnum.M);
//        entity.setStudentPassword("sefse");
//
//        StudentEntity save = RPS.save(entity);
//
//        if (save!=null){
//            Optional<StudentEntity> byId = RPS.findById(studentNum);
//            log.info("id  : {}",byId);
//        }
//
//
//    }
//}