package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnVo;
import com.green.smartgradever2.entity.LectureStudentEntity;
import com.green.smartgradever2.entity.SemesterEntity;
import com.green.smartgradever2.entity.StudentEntity;
import com.green.smartgradever2.entity.StudentSemesterScoreEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface GradeMngmnRepository extends JpaRepository<StudentSemesterScoreEntity, Long> {
}
