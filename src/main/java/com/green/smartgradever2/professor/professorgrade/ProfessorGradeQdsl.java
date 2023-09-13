package com.green.smartgradever2.professor.professorgrade;


import com.green.smartgradever2.admin.model.AdminSelLectureDto;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.professor.professorgrade.model.ProStudentLectureVo;
import com.green.smartgradever2.professor.professorgrade.model.ProStudentListVo;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.ExpressionUtils.orderBy;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
@RequiredArgsConstructor
public class ProfessorGradeQdsl {
    private  final JPAQueryFactory jpaQueryFactory;
    QLectureApplyEntity la =QLectureApplyEntity.lectureApplyEntity;
    QLectureStudentEntity ls = QLectureStudentEntity.lectureStudentEntity;
    QSemesterEntity se = QSemesterEntity.semesterEntity;
    QLectureRoomEntity re = QLectureRoomEntity.lectureRoomEntity;
    QLectureScheduleEntity sd = QLectureScheduleEntity.lectureScheduleEntity;
    QLectureNameEntity ne = QLectureNameEntity.lectureNameEntity;
    QProfessorEntity pf = QProfessorEntity.professorEntity;
    QStudentEntity st = QStudentEntity.studentEntity;



    public Map<String, Object> selProfessoreGradeList(Long iprofessor, Long ilecture, Pageable page) {

        JPQLQuery<ProStudentLectureVo> query = jpaQueryFactory

                .selectDistinct(Projections.bean(ProStudentLectureVo.class, ls.ilectureStudent,
                        la.ilecture,
                        st.studentNum,
                        st.majorEntity.majorName,
                        st.nm.as("studentName"),
                        ls.attendance,
                        ls.midtermExamination,
                        ls.finalExamination,
                        sd.dayWeek,
                        ls.totalScore,
                        ls.finishedYn,
                        ExpressionUtils.as(Expressions.constant("rating"), "rating"),
                        ExpressionUtils.as(Expressions.constant("grade"), "grade")))
                .from(ls)
                .leftJoin(ls.studentEntity ,st)
                .leftJoin(ls.lectureApplyEntity, la)
                .leftJoin(ls.lectureApplyEntity.lectureRoomEntity,re)
                .leftJoin(ls.lectureApplyEntity.lectureScheduleEntity, sd)

                .orderBy(ls.ilectureStudent.desc())


                .offset(page.getOffset())
                .limit(page.getPageSize());

        if (ilecture != null) {
            query = query.where(la.ilecture.eq(ilecture));
        }


        List<ProStudentLectureVo> studentLectureVos = query.fetch();

        // 학점 및 평점을 계산하여 설정
        for (ProStudentLectureVo studentLectureVo : studentLectureVos) {
            GradeUtils gradeUtils = new GradeUtils();
            String customGrade = gradeUtils.totalGradeFromScore1(studentLectureVo.getTotalScore());
            String customRating = gradeUtils.totalGradeFromScore(studentLectureVo.getTotalScore());
            studentLectureVo.setGrade(customGrade);
            studentLectureVo.setRating(customRating);
        }

        PagingUtils pagingUtils = new PagingUtils(page.getPageNumber(), studentLectureVos.size(), page.getPageSize());

        // 반환할 JSON 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("iprofessor", iprofessor);
        response.put("page", pagingUtils);
        response.put("lecturelist", studentLectureVos); ;

        return response;
    }


    public List<ProStudentListVo> getlist(Long iprofessor, Long ilecture, int year, Long studentNum,String nm, Pageable pageable) {

        long totalCount = selectCountWithPaging(iprofessor, ilecture, year, studentNum, nm);

        List<ProStudentListVo> result = jpaQueryFactory
                .selectDistinct(Projections.bean(ProStudentListVo.class,
                        ls.ilectureStudent,
                        la.ilecture,
                        ls.studentEntity.studentNum,
                        ls.studentEntity.phone,
                        ls.studentEntity.gender,
                        ls.studentEntity.nm.as("studentName"),
                        ls.studentEntity.majorEntity.majorName.as("majorName"),
                        ls.studentEntity.grade,
                        ls.lectureApplyEntity.semesterEntity.year))
                .from(ls)
                .join(ls.lectureApplyEntity, la)
                .join(ls.lectureApplyEntity.semesterEntity, se)
                .where(ilecture(ilecture),studentNum(studentNum),yearCondition(year))
                .orderBy(ls.ilectureStudent.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();



      return null;
    }

    private BooleanExpression ilecture(Long ilecture) {
        if (ilecture == null) {
            return null;
        }
        return QLectureApplyEntity.lectureApplyEntity.ilecture.eq(ilecture);
    }

    private BooleanExpression studentNum(Long studentNum) {
        if (studentNum == null) {
            return null;
        }
        return QLectureStudentEntity.lectureStudentEntity.studentEntity.studentNum.eq(studentNum);
    }

    // 추가: year 필터링 조건
    private BooleanExpression yearCondition(Integer year) {
        if (year == null) {
            return null;
        }
        return se.year.eq(year);
    }


//    private JPQLQuery<Long> selCount(Long iprofessor, Long ilecture, int year, Long studentNum, String nm) {
//        JPQLQuery<Long> result = jpaQueryFactory
//                .select(ls.studentEntity.studentNum.count())
//                .from(ls)
//                .leftJoin(ls.lectureApplyEntity.semesterEntity , se)
//                .leftJoin(ls.lectureApplyEntity , la)
//                .where(
//
//                )
//    }

    private Long selectCountWithPaging(Long iprofessor, Long ilecture, int year, Long studentNum, String nm) {

        BooleanExpression conditions = la.professorEntity.iprofessor.eq(iprofessor)
                .and(ilecture(ilecture))
                .and(studentNum(studentNum));

        if (nm != null && !nm.isEmpty()) {
            conditions = conditions.and(ls.studentEntity.nm.contains(nm));
        }

        // 추가: year 필터링 조건
        if (year != 0) {
            conditions = conditions.and(yearCondition(year));
        }

        return jpaQueryFactory
                .select(ls.lectureApplyEntity.ilecture.count())
                .from(ls)
                .join(ls.lectureApplyEntity.semesterEntity)
                .where(conditions)
                .fetchOne();
    }

}



