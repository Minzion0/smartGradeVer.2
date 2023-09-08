package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.student.model.StudentListLectureVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentQdsl {
    private  final JPAQueryFactory jpaQueryFactory;
     QLectureApplyEntity  la =QLectureApplyEntity.lectureApplyEntity;
     QLectureStudentEntity ls = QLectureStudentEntity.lectureStudentEntity;
     QSemesterEntity se = QSemesterEntity.semesterEntity;
     QLectureRoomEntity re = QLectureRoomEntity.lectureRoomEntity;
     QLectureScheduleEntity sd = QLectureScheduleEntity.lectureScheduleEntity;
     QLectureNameEntity ne = QLectureNameEntity.lectureNameEntity;
     QProfessorEntity pf = QProfessorEntity.professorEntity;

    public List<StudentListLectureVo> selStudentLectureList(int openingProceudres,
                                                            int grade, Pageable pageable) {
        JPQLQuery<StudentListLectureVo> query = jpaQueryFactory
                .selectDistinct(Projections.bean(StudentListLectureVo.class, la.ilecture,la.openingProceudres.as("openingProcedures")
                        ,ne.lectureName,ne.ilectureName,ne.score,re.ilectureRoom
                        ,re.buildingName,re.lectureRoomName,se.isemester,sd.lectureStrTime,sd.lectureEndTime
                        ,la.attendance,la.midtermExamination,la.finalExamination,la.lectureMaxPeople
                        ,la.gradeLimit,sd.dayWeek,la.ctnt,la.bookUrl,la.textbook,la.delYn, pf.nm.as("professorName"))
                )
                .from(la).
                 leftJoin(la.lectureRoomEntity, re)
                .leftJoin(la.semesterEntity, se)
                .leftJoin(la.lectureNameEntity , ne)
                .leftJoin(la.lectureScheduleEntity, sd)
                .leftJoin(la.lectureStudentEntity, ls)
                .leftJoin(la.professorEntity,pf)
                .where(la.openingProceudres.eq(openingProceudres).and(la.gradeLimit.loe(grade)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return query.fetch();

    }

}
