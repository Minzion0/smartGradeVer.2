package com.green.smartgradever2.board.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardInsRes {
    private Long iadmin;
    private String ctnt;
    private String title;
    private int importance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int boardView;
}
