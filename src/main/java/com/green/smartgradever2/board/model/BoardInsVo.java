package com.green.smartgradever2.board.model;

import com.green.smartgradever2.config.entity.BoardPicEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardInsVo {
    private Long iboard;
    private Long iadmin;
    private String ctnt;
    private String title;
    private int importance;
    private LocalDateTime updatedAt;
    private List<BoardPicEntity> pisc;
}
