package com.green.smartgradever2.settings.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonRes {
    SUCCESS(0, "Success"), FAIL(-1, "Fail");

    int code;
    String msg;
}
