package com.green.smartgradever2.board.model;

import com.green.smartgradever2.entity.AdminEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRes {
    private Long iboard;
    private AdminEntity iadmin;
    private String ctnt;
    private String title;
    private int importance;
}
