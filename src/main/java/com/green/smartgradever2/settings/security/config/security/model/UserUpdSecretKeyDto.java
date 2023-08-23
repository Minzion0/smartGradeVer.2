package com.green.smartgradever2.settings.security.config.security.model;

import lombok.Data;

@Data
public class UserUpdSecretKeyDto {
    private String role;
    private String secretKey;
    private String uid;
}
