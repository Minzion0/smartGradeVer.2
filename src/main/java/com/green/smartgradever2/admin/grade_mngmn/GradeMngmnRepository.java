package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import com.green.smartgradever2.config.entity.SemesterEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
import com.green.smartgradever2.config.entity.StudentSemesterScoreEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GradeMngmnRepository extends JpaRepository<StudentSemesterScoreEntity, Long> {
    List<StudentSemesterScoreEntity> findAllBySemesterEntity(SemesterEntity semesterEntity);

    @Query(value = "select new com.green.smartgradever2" +
            ".admin.grade_mngmn.model.GradeMngmnDetailVo(s.pic, s.nm, s.gender, s.birthdate, s.phone, s.address," +
            " s.studentNum, m.majorName, s.createdAt, s.email, sssc.score, m.graduationScore)," +
            "  s.pic, s.nm, s.gender, s.birthdate, s.phone, s.address, s.studentNum" +
            ", m.majorName, s.createdAt, s.email, sssc.score, m.graduationScore" +
            ", s.grade, sssc.semesterEntity, lc.lectureApplyEntity" +
            ", sssc.avgScore, avg(sssc.rating)" +
            " from LectureStudentEntity lc " +
            "left join lc.studentEntity s " +
            "inner join s.majorEntity m " +
            "inner join  s.ssscList sssc " +
            "where s.studentNum = :studentNum")
    GradeMngmnDetailVo selStudentDetail(Long studentNum);

    @Query(value = "select " +
            "sssc.grade, sem.semester, lne.lectureName," +
            "p.nm, lne.score , ls.totalScore, :rating " +
            "from LectureStudentEntity ls " +
            "join ls.studentEntity s " +
            "join s.ssscList sssc " +
            "join sssc.semesterEntity sem " +
            "join ls.lectureApplyEntity la " +
            "join la.lectureNameEntity lne " +
            "join la.professorEntity p ")
    List<GradeMngmnVo> selGradeFindStudent(Pageable pageable);

    @Query(value = "select distinct sssc.grade, sem.semester" +
            ", sssc.avgScore, sssc.rating " +
            "from LectureStudentEntity ls " +
            "join ls.studentEntity s " +
            "join s.ssscList sssc " +
            "join sssc.semesterEntity sem")
    List<GradeMngmnAvgVo> selAvg(Pageable pageable);

    GradeMngmnStudentVo findByStudentEntity(StudentEntity student);
}
