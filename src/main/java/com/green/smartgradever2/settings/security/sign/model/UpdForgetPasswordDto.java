package com.green.smartgradever2.settings.security.sign.model;

import lombok.Data;

@Data
public class UpdForgetPasswordDto {
    private String uid;
    private String upw;
    private String role;
}
