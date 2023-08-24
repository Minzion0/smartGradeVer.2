package com.green.smartgradever2.professor;

import com.green.smartgradever2.professor.model.ProfessorProfileDto;
import com.green.smartgradever2.professor.model.ProfessorSelRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor")
public class ProfessorController {
    private  final  ProfessorService SERVICE;


    @GetMapping("/{iprofessor}")
    public ProfessorProfileDto getProfessorProfile(@PathVariable Long iprofessor) {
        return SERVICE.getProfessorProfile(iprofessor);
    }



}
