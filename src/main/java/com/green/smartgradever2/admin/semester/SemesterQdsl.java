package com.green.smartgradever2.admin.semester;

import com.green.smartgradever2.config.entity.QSemesterEntity;
import com.green.smartgradever2.config.entity.SemesterEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SemesterQdsl {

    private final JPAQueryFactory jpaQueryFactory;
    QSemesterEntity semester =QSemesterEntity.semesterEntity;

    public SemesterEntity findSemester(){

        List<SemesterEntity> fetch = jpaQueryFactory
                .selectFrom(semester)
                .orderBy(semester.isemester.desc())
                .fetch();

        SemesterEntity semesterEntity = fetch.get(0);
        return semesterEntity;
    }
}
