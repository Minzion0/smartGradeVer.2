package com.green.smartgradever2.board.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardDetailVo {
    private Long iboard;
    private Long iadmin;
    private String title;
    private String ctnt;
    private int importance;
    private List<BoardPicVo> pisc;
}
