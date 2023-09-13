package com.green.smartgradever2.admin.lecturename;

import com.green.smartgradever2.config.entity.LectureNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureNameRepository extends JpaRepository<LectureNameEntity,Long> {
    LectureNameEntity findByLectureName(String name);
    List<LectureNameEntity> findByLectureNameContains(String lectureName);

}
