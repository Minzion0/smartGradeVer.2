package com.green.smartgradever2.email.model;

import lombok.Data;

@Data
public class CheckEmailDto {
    private Long iuser;
    private String role;
    private String mail;
}
