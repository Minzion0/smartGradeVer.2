package com.green.smartgradever2.board.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardUpdDto {
    private Long iboard;
    private String title;
    private String ctnt;
    private int importance;
    private LocalDateTime updatedAt;
}
