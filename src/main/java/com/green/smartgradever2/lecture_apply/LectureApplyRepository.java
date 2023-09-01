package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.config.entity.SemesterEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface LectureApplyRepository extends JpaRepository<LectureApplyEntity,Long> {
    List<LectureApplyEntity> findByProfessorEntity(ProfessorEntity professor);
    Page<LectureApplyEntity> findAllByProfessorEntityIprofessor(Long iprofessor, Pageable pageable);

    Optional<LectureApplyEntity> findByProfessorEntityIprofessorAndIlecture(Long iprofessor, Long ilecture);

//    List<LectureApplyEntity> findByStudentEntity(StudentEntity student);
  List<LectureApplyEntity> findAllByProfessorEntityAndSemesterEntity(ProfessorEntity professorEntity, SemesterEntity semesterEntity);
}
