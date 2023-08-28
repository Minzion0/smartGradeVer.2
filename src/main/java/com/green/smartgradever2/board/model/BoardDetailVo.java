package com.green.smartgradever2.board.model;

import com.green.smartgradever2.entity.BoardPicEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardDetailVo {
    private Long iboard;
    private Long iadmin;
    private String title;
    private String ctnt;
    private List<String> pisc;
    private int importance;
}
