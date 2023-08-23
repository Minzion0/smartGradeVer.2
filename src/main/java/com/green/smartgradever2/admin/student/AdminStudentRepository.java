package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminStudentRepository extends JpaRepository<StudentEntity,Integer> {
}
