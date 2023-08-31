package com.green.smartgradever2.lectureschedule;

import com.green.smartgradever2.config.entity.LectureScheduleEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LectureScheduleRepository extends JpaRepository<LectureScheduleEntity, Long> {
    LectureScheduleEntity findByIlecture(Long ilecture);
  List<LectureScheduleEntity>findBySemesterEntity(SemesterEntity semesterEntity);
}
