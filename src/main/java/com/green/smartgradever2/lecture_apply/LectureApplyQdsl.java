package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.config.entity.LectureScheduleEntity;
import com.green.smartgradever2.config.entity.QLectureScheduleEntity;
import com.green.smartgradever2.config.entity.QSemesterEntity;
import com.green.smartgradever2.lecture_apply.model.LectureAppllyInsParam;
import com.green.smartgradever2.lecture_apply.model.LectureApplyScheduleVo;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor

public class LectureApplyQdsl {

    private final JPAQueryFactory jpaQueryFactory;
    QLectureScheduleEntity schedule = QLectureScheduleEntity.lectureScheduleEntity;
    QSemesterEntity semester= QSemesterEntity.semesterEntity;

    public List<LectureScheduleEntity> findLectureSchedule(LectureAppllyInsParam param){

        List<LectureScheduleEntity> list = jpaQueryFactory.selectFrom(schedule)
                .groupBy(schedule.ilecture)
                .where(schedule.dayWeek.eq(param.getDayWeek())
                        .and( schedule.lectureApplyEntity.lectureRoomEntity.ilectureRoom.eq(param.getIlectureRoom()))
                        .and(schedule.lectureApplyEntity.semesterEntity.isemester.eq(latestSemester()))
                ).fetch();

        return list;
    }

    public List<LectureScheduleEntity> findLectureRoomSchedule(Long ilectureRoom){



        List<LectureScheduleEntity> list = jpaQueryFactory.select(schedule).from(schedule)
                .join(schedule.lectureApplyEntity).on(schedule.lectureApplyEntity.ilecture.eq(schedule.lectureApplyEntity.ilecture))
                .where(schedule.lectureApplyEntity.lectureRoomEntity.ilectureRoom.eq(ilectureRoom)
                        .and(schedule.lectureApplyEntity.semesterEntity.isemester.eq(latestSemester())
                        )).fetch();

        return list;
    }

    private Long latestSemester(){
        Long latestSemester = jpaQueryFactory.select(semester.isemester.max())
                .from(semester)
                .fetchFirst();
        return latestSemester;
    }
}
