package com.green.smartgradever2.email.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVo {
    private Long id;
    private String role;
    private String optUrl;
    private String secretKey;
}
