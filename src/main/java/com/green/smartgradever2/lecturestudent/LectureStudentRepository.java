package com.green.smartgradever2.lecturestudent;

import com.green.smartgradever2.entity.LectureApplyEntity;
import com.green.smartgradever2.entity.LectureStudentEntity;
import com.green.smartgradever2.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureStudentRepository extends JpaRepository<LectureStudentEntity,Long> {
    List<LectureStudentEntity> findByLectureAppllyEntity ( LectureApplyEntity entity);
    long countByLectureAppllyEntity(LectureApplyEntity lectureApply);
    List<LectureStudentEntity> findByStudentEntity(StudentEntity student);
    List<LectureStudentEntity> findByStudentEntityStudentNumAndFinishedYn(Integer studentNum, int finishedYn);
}
