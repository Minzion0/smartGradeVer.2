package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.entity.MajorEntity;
import com.green.smartgradever2.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminStudentRepository extends JpaRepository<StudentEntity,Integer> {
    List<StudentEntity> findByMajorEntityAndCreatedAtBetween(MajorEntity imajor, LocalDateTime start, LocalDateTime end);
}
