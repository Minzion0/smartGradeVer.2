package com.green.smartgradever2.board.model;

import lombok.Data;

@Data
public class BoardInsDto {
    private Long iadmin;
    private String ctnt;
    private String title;
    private int importance;
}
