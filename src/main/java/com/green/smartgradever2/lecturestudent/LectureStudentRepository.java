package com.green.smartgradever2.lecturestudent;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureStudentRepository extends JpaRepository<LectureStudentEntity,Long> {
    List<LectureStudentEntity> findByLectureApplyEntity(LectureApplyEntity entity);
    long countByLectureApplyEntity(LectureApplyEntity lectureApply);
    List<LectureStudentEntity> findByStudentEntity(StudentEntity student);
    List<LectureStudentEntity> findByStudentEntityStudentNumAndFinishedYn(Integer studentNum, int finishedYn);
    List<LectureStudentEntity> findAllByLectureApplyEntity(LectureApplyEntity entity);


}
