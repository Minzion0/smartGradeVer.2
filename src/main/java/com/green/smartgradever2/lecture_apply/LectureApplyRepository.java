package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.config.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface LectureApplyRepository extends JpaRepository<LectureApplyEntity,Long> {
    List<LectureApplyEntity> findByProfessorEntity(ProfessorEntity professor);
    Page<LectureApplyEntity> findByProfessorEntity(ProfessorEntity professor, Pageable pageable);

    Optional<LectureApplyEntity> findByProfessorEntityIprofessorAndIlecture(Long iprofessor, Long ilecture);

//    List<LectureApplyEntity> findByStudentEntity(StudentEntity student);
  List<LectureApplyEntity> findAllByProfessorEntityAndIlecture(ProfessorEntity professorEntity, Long ilecture ,Pageable pageable);
  List<LectureApplyEntity> findAllByProfessorEntity(ProfessorEntity professorEntity,Pageable pageable);
    List<LectureApplyEntity> findByProfessorEntityAndOpeningProceudres(ProfessorEntity professorEntity, int openingProceudres, Pageable pageable);
    List<LectureApplyEntity> findByProfessorEntityAndLectureNameEntityLectureName(ProfessorEntity professorEntity, String lecturename, Pageable pageable);

}

//    int countLectureStudentsByLectureApplyEntity(LectureApplyEntity lectureApplyEntity);


