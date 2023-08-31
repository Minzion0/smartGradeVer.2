package com.green.smartgradever2.settings.security.config.security;


import com.green.smartgradever2.settings.security.config.security.model.UserEntity;
import com.green.smartgradever2.settings.security.config.security.model.UserSelRoleEmailVo;
import com.green.smartgradever2.settings.security.config.security.model.UserTokenEntity;
import com.green.smartgradever2.settings.security.config.security.model.UserUpdSecretKeyDto;
import com.green.smartgradever2.settings.security.sign.model.SignSelPasswordTrueDto;
import com.green.smartgradever2.settings.security.sign.model.SignSelPasswordTrueVo;
import com.green.smartgradever2.settings.security.sign.model.UpdForgetPasswordDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDetailsMapper {

    /** 유저정보 뽑기 **/
    UserEntity getByUid(String uid, String role);

    /** 유저시크릿키 발급 **/
    UserEntity getUserSecret(String uid, String role);

    /** 유정 이메일 뽑기 **/
    UserSelRoleEmailVo getUserRoleEmail(String uid, String role);

    /** 유정 시크릿 키 새로 발급 **/
    int updSecretKey(UserUpdSecretKeyDto dto);


    SignSelPasswordTrueVo selTruePassword (SignSelPasswordTrueDto dto);

    /** 학생 학번 뽑기 **/
    String getStudentNum(String studentNum);

    /** 비밀번호 임의로 true 값으로 변환 시키기 **/
    int updForgetPasswordTrue (String uid, String role);

    /** 비밀번호 변경 **/
    int updForgetPassword (UpdForgetPasswordDto dto);
}
