package com.green.smartgradever2.admin.lecturename;

import com.green.smartgradever2.entity.LectureNameEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureNameRepository extends JpaRepository<LectureNameEntity,Long> {

    List<LectureNameEntity> findByLectureNameContains(String lectureName);
}
