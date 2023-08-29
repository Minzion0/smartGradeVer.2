package com.green.smartgradever2.professor;

import com.green.smartgradever2.config.entity.ProfessorEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfessorMapper {
    int upProfessor(ProfessorEntity entity);
}
