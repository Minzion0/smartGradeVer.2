package com.green.smartgradever2.admin.professor;

import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProfessorRepository extends JpaRepository<ProfessorEntity,Long> {
    Page<ProfessorEntity> findAllByNm(String nm, Pageable pageable);
    Page<ProfessorEntity> findAllByMajorEntity(MajorEntity majorEntity, Pageable pageable);
    Page<ProfessorEntity> findAllByNmAndMajorEntity(String nm, MajorEntity majorEntity, Pageable pageable);
}
