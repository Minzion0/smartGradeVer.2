package com.green.smartgradever2.settings.security.config.security.otp;


import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

@Component
public class TOTPTokenGenerator {
    private String GOOGLE_URL = "https://www.google.com/chart?chs=200x200&chld=M|0&cht=qr&chl=";

    // 최초 개인 Security Key 생성
    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    // 개인키, 계정명(시스템 사용자 ID), 발급자를 받아서 구글OTP 인증용 링크를 생성
    public String getGoogleAuthenticatorBarcode(String secretKey, String account, String issuer) {
        try {
            return GOOGLE_URL + "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
