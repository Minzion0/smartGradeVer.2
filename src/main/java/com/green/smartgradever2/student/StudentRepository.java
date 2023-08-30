package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity,Integer> {
    StudentEntity findBystudentNum(int studentNum);
    StudentEntity findByStudentNum(Integer studentNum);
}
