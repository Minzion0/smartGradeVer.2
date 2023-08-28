package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.entity.LectureStudentEntity;
import com.green.smartgradever2.entity.StudentSemesterScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface GradeMngmnRepository extends JpaRepository<StudentSemesterScoreEntity, Long> {

}
