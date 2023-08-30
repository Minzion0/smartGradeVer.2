package com.green.smartgradever2.admin.professor;

import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminProfessorRepository extends JpaRepository<ProfessorEntity,Long> {
    Page<ProfessorEntity> findAllByNm(String nm, Pageable pageable);
    Page<ProfessorEntity> findAllByMajorEntity(MajorEntity majorEntity, Pageable pageable);
    Page<ProfessorEntity> findAllByNmAndMajorEntity(String nm, MajorEntity majorEntity, Pageable pageable);
    List<ProfessorEntity> findAllByMajorEntityAndCreatedAtBetween(MajorEntity imajor, LocalDateTime start, LocalDateTime end);
    Optional<List<ProfessorEntity>> findAllByOrderByIprofessor();
}
