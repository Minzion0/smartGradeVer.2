package com.green.smartgradever2.email.model;

import lombok.Data;

@Data
public class EmailDto {
    private Long id;
    private String pw;
    private String role;
    private String optImg;
    private String secretKey;
}
