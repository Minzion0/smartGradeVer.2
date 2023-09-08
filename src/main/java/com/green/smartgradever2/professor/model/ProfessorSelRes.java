package com.green.smartgradever2.professor.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProfessorSelRes {
   private ProfessorProfileDto profile;
   private List<ProfessorLectureVo> lectureList;
   private String deadline;
}
