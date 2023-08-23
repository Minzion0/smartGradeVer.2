package com.green.smartgradever2.professor.model;

import com.green.smartgradever2.entity.ProfessorEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProfessorSelRes {
   List <ProfessorEntity> List;
}
