package com.green.smartgradever2.board.model;

import com.green.smartgradever2.entity.AdminEntity;
import com.green.smartgradever2.entity.BoardEntity;
import lombok.Data;

@Data
public class BoardInsDto {
    private Long iboard;
    private AdminEntity iadmin;
    private String ctnt;
    private String title;
    private int importance;
}
