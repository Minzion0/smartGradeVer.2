package com.green.smartgradever2.board.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardInsVo {
    private Long iboard;
    private Long iadmin;
    private String ctnt;
    private String title;
    private int importance;
    private LocalDateTime updatedAt;
}
