package com.green.smartgradever2.professor;

import com.green.smartgradever2.entity.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesserRepository extends JpaRepository<ProfessorEntity,Long> {
}
