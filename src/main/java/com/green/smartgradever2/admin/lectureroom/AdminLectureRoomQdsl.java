package com.green.smartgradever2.admin.lectureroom;

import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomDto;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomListVo;
import com.green.smartgradever2.admin.lectureroom.model.AdminLectureRoomVo;
import com.green.smartgradever2.config.entity.LectureRoomEntity;
import com.green.smartgradever2.config.entity.QLectureRoomEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminLectureRoomQdsl {
    private final JPAQueryFactory jpaQueryFactory;

    QLectureRoomEntity lr = QLectureRoomEntity.lectureRoomEntity;

    public List<AdminLectureRoomListVo> voList (AdminLectureRoomDto dto, Pageable pageable) {
        JPQLQuery<AdminLectureRoomListVo> query = jpaQueryFactory.select(Projections.bean(AdminLectureRoomListVo.class,lr.ilectureRoom.as("ilectureRoom"),lr.lectureRoomName,lr.buildingName,lr.maxCapacity,lr.delYn))
                .from(lr)
                .where(eqBuildingName(dto.getBuildingName()),eqLectureRoomName(dto.getLectureRoomName()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(lr.ilectureRoom.desc());

        return query.fetch();
    }

    public List<AdminLectureRoomVo> vo () {
        JPQLQuery<AdminLectureRoomVo> query = jpaQueryFactory.selectDistinct(Projections.bean(AdminLectureRoomVo.class,lr.buildingName))
                .from(lr);

        return query.fetch();
    }

    private BooleanExpression eqLectureRoomName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }
        return lr.lectureRoomName.eq(name);
    }

    private BooleanExpression eqBuildingName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }
        return lr.buildingName.eq(name);
    }
}
