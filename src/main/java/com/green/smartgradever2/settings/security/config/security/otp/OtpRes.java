package com.green.smartgradever2.settings.security.config.security.otp;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OtpRes {
    private String secretKey;
    private String barcodeUrl;
}
