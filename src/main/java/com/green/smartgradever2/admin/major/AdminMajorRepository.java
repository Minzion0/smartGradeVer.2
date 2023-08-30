package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.config.entity.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMajorRepository extends JpaRepository<MajorEntity, Long> {
    List<MajorEntity> findAllByDelYnAndMajorName(int delYn, String majorName);
}
