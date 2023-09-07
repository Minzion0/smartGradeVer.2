package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import com.green.smartgradever2.config.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GradeMngmnQdsl {
    private final JPAQueryFactory jpaQueryFactory;
    QStudentSemesterScoreEntity sssc = QStudentSemesterScoreEntity.studentSemesterScoreEntity;
    QStudentEntity st = QStudentEntity.studentEntity;
    QSemesterEntity sem = QSemesterEntity.semesterEntity;
    QLectureStudentEntity ls = QLectureStudentEntity.lectureStudentEntity;
    QLectureApplyEntity la = QLectureApplyEntity.lectureApplyEntity;
    QLectureNameEntity ln = QLectureNameEntity.lectureNameEntity;
    QProfessorEntity pr = QProfessorEntity.professorEntity;
    QMajorEntity m = QMajorEntity.majorEntity;

    public Optional<GradeMngmnDetailVo> studentDetail(GradeMngmnDetailSelDto dto) {
        JPQLQuery<GradeMngmnDetailVo> query = jpaQueryFactory.selectDistinct(Projections.bean(GradeMngmnDetailVo.class,
                        st.nm.as("name"), st.gender, st.phone, st.studentNum, m.majorName, st.createdAt,
                        sssc.score.coalesce(0).as("scoreStudent"), m.graduationScore ))
                .from(st)
                .leftJoin(st.ssscList, sssc)
                .leftJoin(st.majorEntity, m)
                .leftJoin(st.ls, ls)
                .where(eqStudentNum(dto.getStudentNum()));
        return Optional.ofNullable(query.fetchOne());
    }

    public List<GradeMngmnVo> studentVo(GradeMngmnDto dto, Pageable pageable) {
        JPQLQuery<GradeMngmnVo> query = jpaQueryFactory.select(Projections.bean(GradeMngmnVo.class, sssc.grade, sem.semester, ln.lectureName, pr.nm, ln.score, ls.totalScore))
                .from(sssc)
                .join(sssc.studentEntity, st)
                .join(sssc.semesterEntity, sem)
                .join(st.ls, ls)
                .join(ls.lectureApplyEntity, la)
                .join(la.lectureNameEntity, ln)
                .join(la.professorEntity, pr)
                .where(eqStudentNum(dto.getStudentNum()),eqGrade(dto.getGrade()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

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
                .where(eqStudentNum(dto.getStudentNum()));

        return query.fetch();
    }

    public GradeMngmnStudentVo mngmnStudentVo(GradeMngmnDto dto) {
        QStudentEntity st = QStudentEntity.studentEntity;

        JPQLQuery<GradeMngmnStudentVo> query = jpaQueryFactory.select(Projections.bean(GradeMngmnStudentVo.class, st.studentNum, st.nm))
                .from(st)
                .where(eqStudentNum(dto.getStudentNum()));

        return query.fetchOne();
    }

    private BooleanExpression eqStudentNum(Long studentNum) {
        if (StringUtils.isNullOrEmpty(studentNum.toString())) {
            return null;
        }
        return st.studentNum.eq(studentNum);
    }

    private BooleanExpression eqGrade(Integer grade) {
        if (StringUtils.isNullOrEmpty(grade.toString())) {
            return null;
        }
        return st.grade.eq(grade);
    }
}
