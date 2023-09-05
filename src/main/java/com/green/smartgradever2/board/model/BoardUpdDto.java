package com.green.smartgradever2.board.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoardUpdDto {
    private Long iboard;
    private List<Long> ipic;
    private Long iadmin;
    private String ctnt;
    private String title;
    private int importance;
    private LocalDateTime updatedAt;
}
