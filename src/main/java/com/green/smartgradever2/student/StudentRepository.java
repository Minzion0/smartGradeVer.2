package com.green.smartgradever2.student;

import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnStudentVo;
import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity,Long> {
    StudentEntity findBystudentNum(Long studentNum);




}
