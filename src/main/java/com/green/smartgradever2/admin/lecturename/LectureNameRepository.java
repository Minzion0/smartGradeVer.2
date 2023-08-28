package com.green.smartgradever2.admin.lecturename;

import com.green.smartgradever2.entity.LectureNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureNameRepository extends JpaRepository<LectureNameEntity,Long> {
}
