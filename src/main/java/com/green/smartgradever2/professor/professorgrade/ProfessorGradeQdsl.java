package com.green.smartgradever2.professor.professorgrade;


import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.professor.professorgrade.model.ProStudentLectureVo;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.querydsl.core.types.ExpressionUtils.orderBy;

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
}
