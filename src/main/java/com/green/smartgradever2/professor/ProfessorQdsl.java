package com.green.smartgradever2.professor;

import com.green.smartgradever2.config.entity.QLectureApplyEntity;
import com.green.smartgradever2.config.entity.QSemesterEntity;
import com.green.smartgradever2.config.entity.SemesterEntity;
import com.green.smartgradever2.professor.model.ProfessorScheduleVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfessorQdsl {

    private final JPAQueryFactory factory;
    QSemesterEntity semester =QSemesterEntity.semesterEntity;

    QLectureApplyEntity lectureApply = QLectureApplyEntity.lectureApplyEntity;

    public SemesterEntity findSemester(){
        SemesterEntity semesterEntity = factory.selectFrom(semester)
                .where(semester.isemester.eq(latestSemester())).fetchOne();

        return semesterEntity;

    }

    private Long latestSemester(){
        Long latestSemester = factory.select(semester.isemester.max())
                .from(semester)
                .fetchFirst();
        return latestSemester;
    }


    public List<ProfessorScheduleVo> professorScheduleList(Long iprofessor){
        List<ProfessorScheduleVo> list = factory.select(Projections.bean(ProfessorScheduleVo.class,
                        lectureApply.lectureScheduleEntity.dayWeek
                        , lectureApply.lectureScheduleEntity.lectureStrTime.as("startTime")
                        , lectureApply.lectureScheduleEntity.lectureEndTime.as("endTime")
                ,lectureApply.lectureNameEntity.lectureName.as("lectureName")))
                .from(lectureApply)
                .join(lectureApply.lectureScheduleEntity)
                .where(lectureApply.professorEntity.iprofessor.eq(iprofessor).and(lectureApply.semesterEntity.isemester.eq(latestSemester())))
                .orderBy(lectureApply.lectureScheduleEntity.dayWeek.asc()).fetch();

        return list;

    }
}
