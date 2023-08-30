package com.green.smartgradever2.board.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BoardVo {
    private Long iboard;
    private Long iadmin;
    private String title;
    private int importance;
    private LocalDateTime createdAt;
    private Long boardView;
}
