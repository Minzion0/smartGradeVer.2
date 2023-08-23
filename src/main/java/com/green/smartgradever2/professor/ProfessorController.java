package com.green.smartgradever2.professor;

import com.green.smartgradever2.professor.model.ProfessorSelRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/professor")
public class ProfessorController {
    private  final  ProfessorService SERVICE;


    @GetMapping("/list")
    public ProfessorSelRes getProfessorList() {
        return SERVICE.selPro();
    }
}
