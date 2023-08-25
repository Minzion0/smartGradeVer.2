package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.admin.major.model.AdminMajorDto;
import com.green.smartgradever2.admin.major.model.AdminMajorFindRes;
import com.green.smartgradever2.admin.major.model.AdminMajorVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMajorMapper {
    List<AdminMajorVo> selMajor(AdminMajorDto dto);

    int countMajor();
}
