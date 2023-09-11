package com.green.smartgradever2.admin;

import com.green.smartgradever2.admin.model.AdminSelLectureDto;
import com.green.smartgradever2.admin.model.AdminSelLectureVo;
import com.green.smartgradever2.admin.model.AdminStudentYearGenderCountVo;
import com.green.smartgradever2.config.entity.model.GenderEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    int countLceture(AdminSelLectureDto dto);
    List<AdminSelLectureVo> selLecture(AdminSelLectureDto dto);
    List<AdminStudentYearGenderCountVo>genderCount(String gender);
}
