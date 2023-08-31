package com.green.smartgradever2.professor.professorgrade.model;

import lombok.Data;

@Data
public class ProfessorGradeMngmnUpRes {
    private int attendance;
    private int midtermExamination;
    private int finalExamination;
    private int finishedYn;
    private Long ilectureStudent;
    private Long ipofessor;
    private double point;
    private String rating;
    private Long ilecture;
    private String msg;

    public ProfessorGradeMngmnUpRes() {
    }


    public ProfessorGradeMngmnUpRes(ProfessorGradeMngmnUpDto dto) {
        this.ilecture = dto.getIlecture();
        this.attendance = dto.getAttendance();
        this.midtermExamination = dto.getMidtermExamination();
        this.finalExamination = dto.getFinalExamination();

        this.finishedYn = dto.getFinishedYn();
        this.point = dto.getPoint();
        this.rating = dto.getRating();
        this.ilectureStudent = dto.getIlectureStudent();
    }

}
