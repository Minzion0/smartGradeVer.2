package com.green.smartgradever2.board.model;

import com.green.smartgradever2.entity.AdminEntity;
import lombok.Builder;
import lombok.Data;
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
