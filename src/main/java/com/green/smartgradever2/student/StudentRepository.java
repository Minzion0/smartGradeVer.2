package com.green.smartgradever2.student;

import com.green.smartgradever2.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity,Long> {
    StudentEntity findBystudentNum(int studentNum);
}
