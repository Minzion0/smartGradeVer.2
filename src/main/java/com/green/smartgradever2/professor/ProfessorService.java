package com.green.smartgradever2.professor;

import com.green.smartgradever2.entity.ProfessorEntity;
import com.green.smartgradever2.professor.model.ProfessorSelRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorMapper MAPPER;
    private final ProfesserRepository PROREP;

    public ProfessorSelRes upProfessor() {
        List<ProfessorEntity> professorList = PROREP.findAll();

        return ProfessorSelRes.builder()
                .List(professorList)
                .build();

    }


}
