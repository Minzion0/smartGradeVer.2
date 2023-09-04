package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminStudentRepository extends JpaRepository<StudentEntity,Long> {
    List<StudentEntity> findAllByMajorEntityAndCreatedAtBetween(MajorEntity imajor, LocalDateTime start, LocalDateTime end);
    List<StudentEntity> findByMajorEntity(MajorEntity majorEntity);

}
