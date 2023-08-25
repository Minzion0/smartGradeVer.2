package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import com.green.smartgradever2.admin.lectureroom.AdminLectureRoomRepository;
import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.entity.LectureStudentEntity;
import com.green.smartgradever2.entity.MajorEntity;
import com.green.smartgradever2.entity.StudentSemesterScoreEntity;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeMngmnService {

    private final GradeMngmnRepository GM_REP;
    private final AdminMajorRepository M_REP;
    private final AdminLectureRoomRepository LS_REP;
    private final GradeMngmnMapper MAPPER;

    public GradeMngmnRes selGradeMngmn(GradeMngmnDto dto) {
        int maxPage = MAPPER.countStudent();
        PagingUtils utils = new PagingUtils(dto.getPage(), maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<GradeMngmnAvgVo> avg = MAPPER.GradeMngmnAvg(dto);
        GradeMngmnStudentVo vo = MAPPER.selGradeMngmnStudent(dto);
        List<GradeMngmnVo> voList = MAPPER.selGradeFindStudent(dto);

        int point;
        double score;
        String rate;
        for (GradeMngmnVo a : voList) {
            point = a.getTotalScore();
            GradeUtils utils2 = new GradeUtils(point);
            score = utils2.totalScore();
            rate = utils2.totalRating(score);
            a.setRating(rate);
        }

        return GradeMngmnRes.builder()
                .voList(voList)
                .student(vo)
                .avgVo(avg)
                .paging(utils)
                .build();
    }

//    public GradeMngmnDetailVo selStudentDetail(StudentSemesterScoreEntity entity) {
//        GradeMngmnDto dto = new GradeMngmnDto();
//        dto.setStudentNum(entity.getStudentEntity().getStudentNum());
//        Optional<StudentSemesterScoreEntity> byStudentNum = GM_REP.findByStudentNum(dto.getStudentNum());
//        return GradeMngmnDetailVo.builder()
//                .studentNum(byStudentNum.get().getStudentEntity().getStudentNum())
//                .build();
//    }
}
