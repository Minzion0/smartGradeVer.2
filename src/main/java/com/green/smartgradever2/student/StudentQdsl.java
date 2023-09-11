package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.student.model.StudentListLectureVo;
import com.green.smartgradever2.student.model.StudentScheduleVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
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
                                                            int grade, Pageable pageable, long studentNum,String lectureName) {

        List<Long> enrolledLectures = jpaQueryFactory
                .select(la.ilecture)
                .from(ls)
                .where(ls.studentEntity.studentNum.eq(studentNum))
                .fetch();

        // 모든 강의를 가져오고, 학생이 수강하지 않은 강의만 필터링합니다.
        JPQLQuery<StudentListLectureVo> query = jpaQueryFactory

                .selectDistinct(Projections.bean(StudentListLectureVo.class, la.ilecture, la.openingProceudres.as("openingProcedures"), ne.lectureName
                        , ne.ilectureName, ne.score, re.ilectureRoom
                        , re.buildingName, re.lectureRoomName, se.isemester, sd.lectureStrTime, sd.lectureEndTime
                        , la.attendance, la.midtermExamination, la.finalExamination, la.lectureMaxPeople
                        , la.gradeLimit, sd.dayWeek, la.ctnt, la.bookUrl, la.textbook, la.delYn, pf.nm.as("professorName")))

                .from(la)
                .leftJoin(la.lectureRoomEntity, re)
                .leftJoin(la.semesterEntity, se)
                .leftJoin(la.lectureNameEntity, ne)
                .leftJoin(la.lectureScheduleEntity, sd)
                .leftJoin(la.professorEntity, pf)
                .where(la.openingProceudres.eq(openingProceudres)
                        .and(la.gradeLimit.loe(grade))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

// .and(la.ilecture.notIn(enrolledLectures))// 학생이 수강하지 않은 강의만 필터링
        // lectureName이 비어있지 않으면 필터를 추가
        if (StringUtils.isNotBlank(lectureName)) {
            query = query.where(ne.lectureName.eq(lectureName));
        }

        query = query.offset(pageable.getOffset()).limit(pageable.getPageSize());


        // 학생이 각 강의에 등록한 여부에 따라 "registered" 필드 설정
        List<StudentListLectureVo> lectureList = query.fetch();
        for (StudentListLectureVo lecture : lectureList) {
            lecture.setApplyYn(true); // 기본적으로 true로 설정
            if (enrolledLectures.contains(lecture.getIlecture())) {
                lecture.setApplyYn(false); // 학생이 등록한 강의인 경우 false로 설정
            }
        }

        return lectureList;


    }
}
