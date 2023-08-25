package com.green.smartgradever2.professor;

import com.green.smartgradever2.entity.LectureApplyEntity;
import com.green.smartgradever2.entity.ProfessorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface ProfesserRepository extends JpaRepository<ProfessorEntity,Long> {
    Optional<ProfessorEntity> findByMajorEntityImajor(Long imajor);
    ProfessorEntity findByIprofessor(Long iprofessor);

}
