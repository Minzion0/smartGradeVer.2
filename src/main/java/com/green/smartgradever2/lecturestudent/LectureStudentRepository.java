package com.green.smartgradever2.lecturestudent;

import com.green.smartgradever2.entity.LectureApplyEntity;
import com.green.smartgradever2.entity.LectureStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureStudentRepository extends JpaRepository<LectureStudentEntity,Long> {
    long countByLectureAppllyEntity(LectureApplyEntity lectureApply);

}
