package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.config.entity.*;
import io.lettuce.core.dynamic.annotation.Param;
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

    @Query("SELECT lec FROM LectureApplyEntity lec " +
            "WHERE lec.openingProceudres = 2 " +
            "AND lec.gradeLimit <= :grade")
    Page<LectureApplyEntity> getAllProfessorsLecturesWithFilters(@Param("grade") Optional<Integer> grade, Pageable pageable);

//    List<LectureApplyEntity> findByProfessorEntity(Long studentNum);

    List<LectureApplyEntity> findAllByProfessorEntityAndSemesterEntity(ProfessorEntity professorEntity, SemesterEntity semesterEntity);

    @Query("SELECT COUNT(DISTINCT ls.studentEntity.studentNum) FROM LectureStudentEntity ls " +
            "WHERE ls.lectureApplyEntity.ilecture = :lectureId")
    long countStudentsByIlecture(@Param("lectureId") Long lectureId);

    //Page<LectureApplyEntity> findByProfessorEntityAndLectureNameEntity(ProfessorEntity professorEntity, LectureNameEntity lectureNameEntity, Pageable pageable);

    //Page<LectureApplyEntity> findByProfessorEntityAndLectureNameEntity(ProfessorEntity professorEntity, String lectureName, Pageable pageable);

    Page<LectureApplyEntity> findByProfessorEntityAndLectureNameEntityAndLectureScheduleEntity(
            ProfessorEntity professorEntity, LectureNameEntity lectureNameEntity, int year, Pageable pageable);
}



//    int countLectureStudentsByLectureApplyEntity(LectureApplyEntity lectureApplyEntity);


