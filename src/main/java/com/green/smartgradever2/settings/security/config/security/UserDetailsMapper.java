package com.green.smartgradever2.settings.security.config.security;

import com.green.smartGrade.security.config.security.model.UserEntity;
import com.green.smartGrade.security.config.security.model.UserSelRoleEmailVo;
import com.green.smartGrade.security.config.security.model.UserTokenEntity;
import com.green.smartGrade.security.config.security.model.UserUpdSecretKeyDto;
import com.green.smartGrade.security.sign.model.SignSelPasswordTrueDto;
import com.green.smartGrade.security.sign.model.SignSelPasswordTrueVo;
import com.green.smartGrade.security.sign.model.UpdForgetPasswordDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDetailsMapper {
//    int save(UserEntity p);
    UserEntity getByUid(String uid, String role);
    UserEntity getUserSecret(String uid, String role);

    int updUserToken(UserTokenEntity p);
    UserTokenEntity selUserToken(UserTokenEntity p);

    UserSelRoleEmailVo getUserRoleEmail(String uid, String role);
    int updSecretKey(UserUpdSecretKeyDto dto);
    int updForgetPassword (UpdForgetPasswordDto dto);
    int updForgetPasswordTrue (String uid, String role);
    SignSelPasswordTrueVo selTruePassword (SignSelPasswordTrueDto dto);
    String getStudentNum(String studentNum);
}
