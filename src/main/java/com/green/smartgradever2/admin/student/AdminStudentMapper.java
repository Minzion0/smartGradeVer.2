package com.green.smartgradever2.admin.student;

import com.green.smartgradever2.admin.student.model.AdminStudentFindDto;
import com.green.smartgradever2.admin.student.model.AdminStudentFindVo;
import com.green.smartgradever2.admin.student.model.AdminStudentLectureVo;
import com.green.smartgradever2.admin.student.model.AdminStudentProfileVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminStudentMapper {

    List<AdminStudentFindVo> findStudents(AdminStudentFindDto Dto);

    int countStudents(AdminStudentFindDto dto);

   AdminStudentProfileVo studentProfile(int studentNum);

   List<AdminStudentLectureVo>studentLectures(int studentNum);

    int promotionGrade();

}
