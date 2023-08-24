package com.green.smartgradever2.board.model;

import com.green.smartgradever2.entity.AdminEntity;
import lombok.Data;

@Data
public class BoardVo {
    private Long iboard;
    private AdminEntity iadmin;
    private String title;
    private int importance;
    private Long boardView;
}
