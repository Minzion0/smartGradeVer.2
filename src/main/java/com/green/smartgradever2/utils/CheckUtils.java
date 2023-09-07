package com.green.smartgradever2.utils;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@Builder
public class CheckUtils {
    private String gender;
    private String email;
    private String phoneNum;
    private String msg;
    private String nm;


    public boolean nmCheck(){
        String nm=this.nm.trim();
        if (nm==null||nm==""){
            return false;
        }
        return true;
    }


    public boolean emailCheck(){
        String regx = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern patten = Pattern.compile(regx);

        Matcher matcher = patten.matcher(email);

        //정상적인 이메일형식이면 true,아니면 false
        return matcher.matches();
    }

    public boolean phoneCheck(){
        String pattern = "^01([0|1|6|7|8|9])-(\\d{3,4})-(\\d{4})$";

        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(phoneNum);


        return matcher.matches();

    }

    public String getMsg(){
        List<String> temp= new ArrayList<>();
        if (this.nm!=null){
            if (!nmCheck()){
                temp.add("이름");
            }
        }

        if (!phoneCheck()){
           temp.add("전화번호") ;
        }
        if (this.email!=null){
            if (!emailCheck()){
                temp.add("이메일");
            }
        }

        if (temp.size()==0){
            return null;
        }

        return temp.toString();
    }

}
