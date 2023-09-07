package com.green.smartgradever2.email.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckEmailVo {
    private Long iuser;
    private String mail;
}
