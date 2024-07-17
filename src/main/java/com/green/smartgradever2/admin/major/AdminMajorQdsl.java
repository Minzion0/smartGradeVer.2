package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorDto;
import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import com.green.smartgradever2.config.entity.QMajorEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminMajorQdsl {
    private final JPAQueryFactory jpaQueryFactory;
    QMajorEntity m = QMajorEntity.majorEntity;

    /** 전공 목록 조회 **/
    public List<AdminMajorVo> majorVos(AdminMajorDto dto, Pageable pageable) {
        JPAQuery<AdminMajorVo> query = jpaQueryFactory.select(Projections.bean(AdminMajorVo.class, m.imajor
                , m.majorName, m.graduationScore, m.delYn, m.remarks))
            .from(m)
            .where(eqMajorName(dto.getMajorName()), eqDelYn(dto.getDelYn()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(m.imajor.desc());
        return query.fetch();
    }

    /** 전공 이름 조건 **/
    private BooleanExpression eqMajorName(String name) {
        if (StringUtils.isNullOrEmpty(name)) {
            return null;
        }
        return m.majorName.contains(name);
    }

    /** 삭제 여부 조건 **/
    private BooleanExpression eqDelYn(Integer delYn) {
        if (delYn == null) {
            return null;
        }
        return m.delYn.eq(delYn);
    }
}