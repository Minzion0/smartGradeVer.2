package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.entity.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<MajorEntity,Long> {
}
