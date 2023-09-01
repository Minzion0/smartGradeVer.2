package com.green.smartgradever2.board.model;

import lombok.Data;

@Data
public class BoardInsParam {
    private String ctnt;
    private String title;
    private int importance;
    private Long iadmin;
}
