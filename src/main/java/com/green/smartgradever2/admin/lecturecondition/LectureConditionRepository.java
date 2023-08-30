package com.green.smartgradever2.admin.lecturecondition;

import com.green.smartgradever2.config.entity.LectureConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureConditionRepository extends JpaRepository<LectureConditionEntity,Long> {
}
