package com.green.smartgradever2.lecturestudent;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LectureStudentRepository extends JpaRepository<LectureStudentEntity,Long> {
    List<LectureStudentEntity> findByLectureApplyEntity(LectureApplyEntity entity);
    long countByLectureApplyEntity(LectureApplyEntity lectureApply);
    List<LectureStudentEntity> findByStudentEntity(StudentEntity student);
    List<LectureStudentEntity> findByStudentEntityStudentNumAndFinishedYn(Long studentNum, int finishedYn);
    List<LectureStudentEntity> findAllByLectureApplyEntity(LectureApplyEntity entity);
    List<LectureStudentEntity> findAllByStudentEntityAndFinishedYn(StudentEntity entity, int finishedYn);
    boolean existsByStudentEntityAndLectureApplyEntity(StudentEntity student, LectureApplyEntity lectureApply);
    LectureStudentEntity findByStudentEntityStudentNumAndIlectureStudent(Long studentNum, Long ilectureStudent);

    List<LectureStudentEntity> findByLectureApplyEntityIlectureAndObjection(Long ilecture, int objection);

    LectureStudentEntity findByLectureApplyEntityIlectureAndIlectureStudent(Long ilecture, Long ilectureStudent);
}
