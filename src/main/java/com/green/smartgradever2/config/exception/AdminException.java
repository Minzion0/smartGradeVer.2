package com.green.smartgradever2.config.exception;

import lombok.Getter;


@Getter

public class AdminException extends RuntimeException {

    private  String   msg;

    public AdminException(String msg){
        this.msg=msg;
    }

}
