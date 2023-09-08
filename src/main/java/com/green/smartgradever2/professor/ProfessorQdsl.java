package com.green.smartgradever2.professor;

import com.green.smartgradever2.config.entity.QSemesterEntity;
import com.green.smartgradever2.config.entity.SemesterEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfessorQdsl {

    private final JPAQueryFactory factory;
    QSemesterEntity semester =QSemesterEntity.semesterEntity;

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
}
