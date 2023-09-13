package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.config.entity.*;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface LectureApplyRepository extends JpaRepository<LectureApplyEntity,Long> {
    List<LectureApplyEntity> findByProfessorEntity(ProfessorEntity professor);
    List<LectureApplyEntity> findByProfessorEntityBetween(ProfessorEntity professor, Pageable pageable, int startopeningProceudres ,int endopeningProceudres);
    Page<LectureApplyEntity> findByProfessorEntity(ProfessorEntity professor, Pageable pageable);
//    List<LectureApplyEntity> findByProfessorEntityAndLectureName(ProfessorEntity entity, String lectureName, Pageable pageable);

    Optional<LectureApplyEntity> findByProfessorEntityIprofessorAndIlecture(Long iprofessor, Long ilecture);

//    List<LectureApplyEntity> findByStudentEntity(StudentEntity student);
  Page<LectureApplyEntity> findAllByProfessorEntityAndIlecture(ProfessorEntity professorEntity, Long ilecture ,Pageable pageable);
    Page<LectureApplyEntity> findAllByProfessorEntityAndLectureStudentEntity(ProfessorEntity professorEntity, LectureStudentEntity entity ,Pageable pageable);
  Page<LectureApplyEntity> findAllByProfessorEntity(ProfessorEntity professorEntity,Pageable pageable);
    List<LectureApplyEntity> findByProfessorEntityAndOpeningProceudresBetween(ProfessorEntity professorEntity, Pageable pageable, int startopeningProceudres ,int endopeningProceudres );
    List<LectureApplyEntity> findByProfessorEntityAndLectureNameEntityLectureNameBetween(ProfessorEntity professorEntity, String lecturename, Pageable pageable,int startopeningProceudres ,int endopeningProceudres  );

    @Query("SELECT lec FROM LectureApplyEntity lec " +
            "WHERE lec.openingProceudres = 2 " +
            "AND lec.gradeLimit <= :grade")
    Page<LectureApplyEntity> getAllProfessorsLecturesWithFilters(@Param("grade") Optional<Integer> grade, Pageable pageable);



    List<LectureApplyEntity> findAllByProfessorEntityAndSemesterEntity(ProfessorEntity professorEntity, SemesterEntity semesterEntity);

    @Query("SELECT COUNT(DISTINCT ls.studentEntity.studentNum) FROM LectureStudentEntity ls " +
            "WHERE ls.lectureApplyEntity.ilecture = :lectureId")
    long countStudentsByIlecture(@Param("lectureId") Long lectureId);


    Page<LectureApplyEntity> findByProfessorEntityAndLectureNameEntityAndSemesterEntity(
     ProfessorEntity professorEntity, LectureNameEntity lectureNameEntity, int year, Pageable pageable);
    Page<LectureApplyEntity> findAll(Specification<LectureApplyEntity> spec, Pageable pageable);

    @Query("SELECT la FROM LectureApplyEntity la WHERE la.professorEntity = :professorEntity AND la.lectureNameEntity = :lectureName")
    Page<LectureApplyEntity> findByProfessorEntityAndLectureNameEntity(@Param("professorEntity") ProfessorEntity professorEntity, @Param("lectureName") String lectureName, Pageable pageable);
}



