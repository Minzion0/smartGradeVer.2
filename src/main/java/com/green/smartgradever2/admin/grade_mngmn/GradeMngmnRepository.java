package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.config.entity.SemesterEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.config.entity.StudentSemesterScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GradeMngmnRepository extends JpaRepository<StudentSemesterScoreEntity, Long> {
    List<StudentSemesterScoreEntity> findAllBySemesterEntity(SemesterEntity semesterEntity);

}
