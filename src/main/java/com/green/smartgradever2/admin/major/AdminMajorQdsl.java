package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.grade_mngmn.model.GradeMngmnVo;
import com.green.smartgradever2.admin.major.model.AdminMajorDto;
import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.config.entity.QMajorEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.runtime.Desc;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.nullExpression;
import static com.querydsl.core.types.dsl.Expressions.simplePath;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminMajorQdsl {
    private final JPAQueryFactory jpaQueryFactory;
    QMajorEntity m = QMajorEntity.majorEntity;

    public List<AdminMajorVo> majorVos(AdminMajorDto dto, Pageable pageable) {
        JPAQuery<AdminMajorVo> query = jpaQueryFactory.select(Projections.bean(AdminMajorVo.class, m.imajor
                        , m.majorName, m.graduationScore, m.delYn, m.remarks))
                .from(m)
                .where(eqMajorName(dto.getMajorName())
                ,eqDelYn(dto.getDelYn()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(m.imajor.desc());
        return query.fetch();
    }

    private BooleanExpression eqMajorName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }
        return m.majorName.contains(name);
    }

    private BooleanExpression eqDelYn(Integer delYn) {
        if (delYn == null) {
            return null;
        }
        return m.delYn.eq(delYn);
    }
}
