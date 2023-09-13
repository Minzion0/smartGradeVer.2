package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.student.model.StudentListLectureVo;
import com.green.smartgradever2.student.model.StudentScheduleVo;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     QStudentEntity sdt = QStudentEntity.studentEntity;

    public Page<StudentListLectureVo> selStudentLectureList(int openingProceudres,
                                                            int grade, Pageable pageable, long studentNum, String lectureName) {
        Map<Long, Long> enrolledStudentsMap = jpaQueryFactory
                .select(la.ilecture, ls.studentEntity.count())
                .from(la)
                .leftJoin(ls).on(la.ilecture.eq(ls.lectureApplyEntity.ilecture))
                .groupBy(la.ilecture)
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(la.ilecture),
                        tuple -> tuple.get(ls.studentEntity.count())
                ));

        //학생이 수강한 강의 목록 가져온다.
        List<Long> enrolledLectures = jpaQueryFactory
                .select(la.ilecture)
                .from(ls)
                .where(ls.studentEntity.studentNum.eq(studentNum))
                .fetch();

        // 모든 강의를 가져온다
        List<StudentListLectureVo> lectureList  = jpaQueryFactory
                .selectDistinct(Projections.bean(StudentListLectureVo.class, la.ilecture, la.openingProceudres.as("openingProcedures"), ne.lectureName
                        , ne.ilectureName, ne.score, re.ilectureRoom
                        , re.buildingName, re.lectureRoomName, se.isemester, sd.lectureStrTime, sd.lectureEndTime
                        , la.attendance, la.midtermExamination, la.finalExamination, la.lectureMaxPeople
                        , la.gradeLimit, sd.dayWeek, la.ctnt, la.bookUrl, la.textbook, la.delYn, pf.nm.as("professorName")
                       ))
                .from(la)
                .leftJoin(la.lectureRoomEntity, re)
                .leftJoin(la.semesterEntity, se)
                .leftJoin(la.lectureNameEntity, ne)
                .leftJoin(la.lectureScheduleEntity, sd)
                .leftJoin(la.professorEntity, pf)
                .where(la.openingProceudres.eq(openingProceudres)
                        .and(la.gradeLimit.loe(grade).and(se.isemester.eq(latestSemester()))
                                .and(lectureName(lectureName))
                ))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();


        for (StudentListLectureVo lecture : lectureList) {
            Long enrolledStudents = enrolledStudentsMap.getOrDefault(lecture.getIlecture(), 0L);
            lecture.setStudentsEnrolled(enrolledStudents);
            lecture.setApplyYn(true); // 등록이 가능한 강의인 true로 설정
            if (enrolledLectures.contains(lecture.getIlecture())) {
                lecture.setApplyYn(false); // 학생이 등록한 강의인 경우 false로 설정
            } else if (lecture.getLectureMaxPeople() != null && lecture.getLectureMaxPeople() <= enrolledLectures.size()) {
                lecture.setApplyYn(false); // 강의가 가득 찬 경우 false로 설정
            }
        }
        JPAQuery<Long> longJPAQuery = selLectureCount(openingProceudres, grade, lectureName);

        return PageableExecutionUtils.getPage(lectureList, pageable, () -> longJPAQuery.fetchOne());

    }

    private BooleanExpression lectureName(String lectureName){
        if (StringUtils.isNullOrEmpty(lectureName)){
            return null;
        }
        return ne.lectureName.eq(lectureName);
    }


    private JPAQuery<Long> selLectureCount(int openingProcedures, int grade,String lectureName) {
        return jpaQueryFactory
                .select(QLectureApplyEntity.lectureApplyEntity.ilecture.count())
                .from(QLectureApplyEntity.lectureApplyEntity)
                .leftJoin(QLectureApplyEntity.lectureApplyEntity.professorEntity)
                .where(la.openingProceudres.eq(openingProcedures)
                        .and(la.gradeLimit.loe(grade).and(se.isemester.eq(latestSemester()))
                                .and(lectureName(lectureName))));
    }







    public List<StudentScheduleVo> findStudentSchedule(Long studentNum){
        List<StudentScheduleVo> fetch = jpaQueryFactory.select(Projections.bean(StudentScheduleVo.class,
                        sd.lectureStrTime.as("startTime")
                        , sd.lectureEndTime.as("endTime")
                        , sd.dayWeek
                        , sd.lectureApplyEntity.lectureNameEntity.lectureName
                        ,ls.lectureApplyEntity.lectureRoomEntity.lectureRoomName
                ,ls.lectureApplyEntity.lectureRoomEntity.buildingName))
                .from(ls)
                .join(ls.lectureApplyEntity)
                .join(ls.lectureApplyEntity.lectureScheduleEntity)
                .where(ls.studentEntity.studentNum.eq(studentNum).and(ls.lectureApplyEntity.semesterEntity.isemester.eq(latestSemester())).and(ls.lectureApplyEntity.openingProceudres.eq(3)))
                .orderBy(sd.dayWeek.asc(),sd.lectureStrTime.asc()).fetch();

        return fetch;
    }

    private Long latestSemester(){
        Long latestSemester = jpaQueryFactory.select(se.isemester.max())
                .from(se)
                .fetchFirst();
        return latestSemester;
    }
}
