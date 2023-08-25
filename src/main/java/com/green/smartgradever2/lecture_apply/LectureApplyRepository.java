package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.entity.LectureApplyEntity;
import com.green.smartgradever2.entity.ProfessorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface LectureApplyRepository extends JpaRepository<LectureApplyEntity,Long> {
    List<LectureApplyEntity> findByProfessorEntity(ProfessorEntity professor);
    Page<LectureApplyEntity> findAllByProfessorEntityIprofessor(Long iprofessor, Pageable pageable);
}
