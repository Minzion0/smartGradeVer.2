package com.green.smartgradever2.professor.professorgrade.model;

public class GradeExceedsMaxScoreException extends RuntimeException {
    public GradeExceedsMaxScoreException(String message) {
        super(message);
    }
}