package com.green.smartgradever2.utils;

public class PathUtils {

    public  String  getPath(){
        StackTraceElement element = Thread.currentThread().getStackTrace()[1];
        String methodName = element.getMethodName();
        String className = element.getClassName().substring(element.getClassName().lastIndexOf(".")+1);

        String path = String.format("class :%s / method : %s ", className, methodName);
        return path;
    }

}
