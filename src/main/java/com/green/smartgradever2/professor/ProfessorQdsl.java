package com.green.smartgradever2.professor;

import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.professor.model.ProfessorScheduleVo;
import com.green.smartgradever2.professor.model.ProfessorSelAllDto;
import com.green.smartgradever2.professor.model.ProfessorSelLectureDto;
import com.green.smartgradever2.professor.model.ProfessorStudentData;
import com.green.smartgradever2.professor.professorgrade.model.ProStudentLectureVo;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfessorQdsl {

    private final JPAQueryFactory factory;
    QSemesterEntity semester = QSemesterEntity.semesterEntity;
    QStudentEntity sd = QStudentEntity.studentEntity;
    QLectureApplyEntity lectureApply = QLectureApplyEntity.lectureApplyEntity;
    QLectureStudentEntity ls = QLectureStudentEntity.lectureStudentEntity;
    QMajorEntity mj = QMajorEntity.majorEntity;
    QLectureNameEntity ne = QLectureNameEntity.lectureNameEntity;

    public SemesterEntity findSemester() {
        SemesterEntity semesterEntity = factory.selectFrom(semester)
                .where(semester.isemester.eq(latestSemester())).fetchOne();

        return semesterEntity;

    }

    private Long latestSemester() {
        Long latestSemester = factory.select(semester.isemester.max())
                .from(semester)
                .fetchFirst();
        return latestSemester;
    }


    public List<ProfessorScheduleVo> professorScheduleList(Long iprofessor) {
        List<ProfessorScheduleVo> list = factory.select(Projections.bean(ProfessorScheduleVo.class,
                        lectureApply.lectureScheduleEntity.dayWeek
                        , lectureApply.lectureScheduleEntity.lectureStrTime.as("startTime")
                        , lectureApply.lectureScheduleEntity.lectureEndTime.as("endTime")
                        , lectureApply.lectureNameEntity.lectureName.as("lectureName")
                        , lectureApply.lectureRoomEntity.lectureRoomName
                        , lectureApply.lectureRoomEntity.buildingName))
                .from(lectureApply)
                .join(lectureApply.lectureScheduleEntity)
                .where(lectureApply.professorEntity.iprofessor.eq(iprofessor).and(lectureApply.semesterEntity.isemester.eq(latestSemester())).and(lectureApply.openingProceudres.eq(3)))
                .orderBy(lectureApply.lectureScheduleEntity.dayWeek.asc()).fetch();

        return list;

    }

    public List<ProfessorStudentData> getStudentObjection(Long ilecture, Long iprofessr, Pageable pageable) {
        JPAQuery<ProfessorStudentData> query = factory
                .selectDistinct(Projections.bean(ProfessorStudentData.class,
                        sd.studentNum,
                        sd.nm.as("studentName"),
                        ls.totalScore,
                        mj.majorName,
                        ls.ilectureStudent, ls.correctionAt, ne.lectureName,
                        ExpressionUtils.as(Expressions.constant("grade"), "grade")))
                .from(ls)
                .leftJoin(ls.studentEntity, sd)
                .leftJoin(ls.studentEntity.majorEntity, mj)
                .leftJoin(ls.lectureApplyEntity.lectureNameEntity, ne)
                .where(ls.objection.eq(1).and(ls.lectureApplyEntity.professorEntity.iprofessor.eq(iprofessr)));

        if (ilecture != null) {
            query.where(ls.lectureApplyEntity.ilecture.eq(ilecture));
        }

        query.orderBy(ls.correctionAt.asc());

        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<ProfessorStudentData> studentLectureVos = query.fetch();

        for (ProfessorStudentData professorStudentData : studentLectureVos) {
            GradeUtils gradeUtils = new GradeUtils();
            String customGrade = gradeUtils.totalGradeFromScore1(professorStudentData.getTotalScore());
            professorStudentData.setGrade(customGrade);
        }

        return studentLectureVos;

    }
}
