package com.green.smartgradever2.lectureschedule;

import com.green.smartgradever2.config.entity.LectureScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureScheduleRepository extends JpaRepository<LectureScheduleEntity, Long> {
    LectureScheduleEntity findByIlecture(Long ilecture);
}
