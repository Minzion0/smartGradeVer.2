package com.green.smartgradever2.admin.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUpdLectureRes {
    private Long ilecture;
    private int procedures;
    private String ctnt;


}