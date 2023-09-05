package com.green.smartgradever2.board.model;

import lombok.Data;

import java.util.List;

@Data
public class BoardUpdParam {
    private Long iboard;
    private String ctnt;
    private String title;
    private int importance;
    private Long iadmin;
    private List<Long> ipic;
}
