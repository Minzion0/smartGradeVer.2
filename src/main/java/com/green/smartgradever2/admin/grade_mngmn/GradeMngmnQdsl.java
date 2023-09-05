package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnAvgVo;
import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnDto;
import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnStudentVo;
import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnVo;
import com.green.smartgradever2.config.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GradeMngmnQdsl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<GradeMngmnVo> studentVo(GradeMngmnDto dto) {
        QStudentSemesterScoreEntity sssc = QStudentSemesterScoreEntity.studentSemesterScoreEntity;
        QStudentEntity st = QStudentEntity.studentEntity;
        QSemesterEntity sem = QSemesterEntity.semesterEntity;
        QLectureStudentEntity ls = QLectureStudentEntity.lectureStudentEntity;
        QLectureApplyEntity la = QLectureApplyEntity.lectureApplyEntity;
        QLectureNameEntity ln = QLectureNameEntity.lectureNameEntity;
        QProfessorEntity pr = QProfessorEntity.professorEntity;

        JPQLQuery<GradeMngmnVo> query = jpaQueryFactory.select(Projections.bean(GradeMngmnVo.class, sssc.grade, sem.semester, ln.lectureName, pr.nm, ln.score, ls.totalScore))
                .from(sssc)
                .join(sssc.studentEntity, st)
                .join(sssc.semesterEntity, sem)
                .join(st.ls, ls)
                .join(ls.lectureApplyEntity, la)
                .join(la.lectureNameEntity, ln)
                .join(la.professorEntity, pr)
                .where(st.studentNum.eq(dto.getStudentNum())
                        ,sssc.grade.eq(dto.getGrade()))
                .limit(dto.getStaIdx());

        return query.fetch();

    }

    public List<GradeMngmnAvgVo> avgVo(GradeMngmnDto dto) {
        QStudentEntity st = QStudentEntity.studentEntity;
        QLectureStudentEntity ls = QLectureStudentEntity.lectureStudentEntity;
        QStudentSemesterScoreEntity sssc = QStudentSemesterScoreEntity.studentSemesterScoreEntity;
        QSemesterEntity sem = QSemesterEntity.semesterEntity;

        JPQLQuery<GradeMngmnAvgVo> query = jpaQueryFactory.select(Projections.bean(GradeMngmnAvgVo.class, sssc.grade, sem.semester, sssc.avgScore, sssc.rating))
                .from(sssc)
                .join(sssc.studentEntity, st)
                .join(st.ls, ls)
                .join(sssc.semesterEntity, sem)
                .where(st.studentNum.eq(dto.getStudentNum()));

        return query.fetch();
    }

    public GradeMngmnStudentVo mngmnStudentVo(GradeMngmnDto dto) {
        QStudentEntity st = QStudentEntity.studentEntity;

        JPQLQuery<GradeMngmnStudentVo> query = jpaQueryFactory.select(Projections.bean(GradeMngmnStudentVo.class, st.nm, st.studentNum))
                .from(st)
                .where(st.studentNum.eq(dto.getStudentNum()));

        return query.fetchOne();
    }
}
