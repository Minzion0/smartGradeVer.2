package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.model.AdminSelLectureDto;
import com.green.smartgradever2.admin.model.AdminSelLectureVo;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.student.model.StudentScheduleVo;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminQdsl {

    private final JPAQueryFactory jpaQueryFactory;

    QLectureApplyEntity apply =QLectureApplyEntity.lectureApplyEntity;
    QLectureRoomEntity room = QLectureRoomEntity.lectureRoomEntity;
    QProfessorEntity professor =QProfessorEntity.professorEntity;
    QLectureNameEntity lectureName =QLectureNameEntity.lectureNameEntity;
    QLectureStudentEntity lectureStudent =QLectureStudentEntity.lectureStudentEntity;
    QSemesterEntity semester =QSemesterEntity.semesterEntity;
    QMajorEntity major =QMajorEntity.majorEntity;
    QLectureScheduleEntity lectureSchedule =QLectureScheduleEntity.lectureScheduleEntity;

   public Page<AdminSelLectureVo> selLecture(AdminSelLectureDto dto, Pageable pageable){


        List<AdminSelLectureVo> limit = jpaQueryFactory.select(Projections.bean(
                        AdminSelLectureVo.class
                        , apply.ilecture
                        , apply.lectureNameEntity.lectureName.as("lectureNm")
                        , semester.semester
                        , major.majorName
                        , apply.professorEntity.nm
                        , apply.lectureRoomEntity.lectureRoomName.as("lectureRoomNm")
                        , apply.lectureRoomEntity.buildingName.as("buildingNm")
                        , apply.gradeLimit
                        , lectureName.score
                        , apply.semesterEntity.semesterStrDate.as("strDate")
                        , apply.semesterEntity.semesterEndDate.as("endDate")
                        , lectureSchedule.lectureStrTime.as("strTime")
                        , lectureSchedule.lectureEndTime.as("endTime")
                        , apply.lectureMaxPeople.as("maxPeople")
                        , apply.openingProceudres.as("procedures")
                        ,lectureSchedule.dayWeek
                        , apply.delYn
                        ,lectureStudent.count().as("currentPeople")))
                //ExpressionUtils.as(jpaQueryFactory.select(lectureStudent.ilectureStudent.count()).from(lectureStudent).where(procedures(dto.getProcedures()),professorName(dto.getNm())),"currentPeople"))
                .from(apply)
                .join(apply.lectureRoomEntity)
                .join(apply.professorEntity)
                .join(apply.lectureNameEntity)
                .leftJoin(lectureStudent).on(apply.ilecture.eq(lectureStudent.lectureApplyEntity.ilecture))
                .leftJoin(semester).on(apply.semesterEntity.isemester.eq(semester.isemester))
                .leftJoin(major).on(apply.professorEntity.majorEntity.imajor.eq(major.imajor))
                .leftJoin(lectureSchedule).on(apply.ilecture.eq(lectureSchedule.ilecture))
                .where(
                        procedures(dto.getProcedures())
                        ,professorName(dto.getNm())
                        ,ilectureName(dto.getIlectureName())
                )
                .orderBy(apply.ilecture.desc())
                .groupBy(apply.ilecture)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();


        JPAQuery<Long> longJPAQuery = selLectureCount(dto);

        return PageableExecutionUtils.getPage(limit, pageable, () -> longJPAQuery.fetchOne());
    }

    private JPAQuery<Long> selLectureCount(AdminSelLectureDto dto){
        JPAQuery<Long> result = jpaQueryFactory
                .select(apply.ilecture.count())
                .from(apply)
                .join(apply.professorEntity)
                .where(
                        procedures(dto.getProcedures())
                        , professorName(dto.getNm())
                        , ilectureName(dto.getIlectureName())
                );
        return result;
    }


    private BooleanExpression procedures(Integer procedures){

        if (procedures==-2){
            return apply.openingProceudres.between(0,2);
        }
        if (procedures >= 0){
            return apply.openingProceudres.eq(procedures);
        }
        return null;
    }

    private BooleanExpression professorName(String name){
        if (StringUtils.isNullOrEmpty(name)){
            return null;
        }
        return professor.nm.contains(name);
    }

    private BooleanExpression ilectureName(Long ilectureName){
        if (ilectureName==0){
            return null;
        }
        return lectureName.ilectureName.eq(ilectureName);
    }





}
