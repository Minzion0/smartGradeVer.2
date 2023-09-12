package com.green.smartgradever2.admin.semester;

import com.green.smartgradever2.config.entity.SemesterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterRepository extends JpaRepository<SemesterEntity,Long> {
    List<SemesterEntity>findByYear(int year);

}
