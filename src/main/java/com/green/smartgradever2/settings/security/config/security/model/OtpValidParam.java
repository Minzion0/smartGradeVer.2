package com.green.smartgradever2.settings.security.config.security.model;

import lombok.Data;

@Data
public class OtpValidParam {
    private String otpNum;
    private String uid;
    private String role;
}
