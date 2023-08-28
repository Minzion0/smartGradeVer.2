package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import com.green.smartgradever2.admin.lectureroom.AdminLectureRoomRepository;
import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.entity.StudentSemesterScoreEntity;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeMngmnService {

    private final GradeMngmnRepository GM_REP;
    private final AdminMajorRepository M_REP;
    private final AdminLectureRoomRepository ALR_REP;
    private final LectureStudentRepository LS_REP;
    private final GradeMngmnMapper MAPPER;

    public GradeMngmnRes postGradeMngmn(GradeMngmnInsDto dto) {
        GradeMngmnDto mngmnDto = new GradeMngmnDto();
        int result = MAPPER.insGradeMngmn(dto);
        List<GradeMngmnVo> voList = MAPPER.selGradeFindStudent(mngmnDto);
        int point;
        double score;
        for (GradeMngmnVo a : voList) {
            point = a.getTotalScore();
            GradeUtils utils2 = new GradeUtils(point);
            score = utils2.totalScore();

            if (result == 1) {
                GradeMngmnRes.builder()
                        .isemester(dto.getIsemester())
                        .studentNum(dto.getStudentNum())
                        .ilectureName(dto.getIlectureName())
                        .semester(dto.getSemester())
                        .avgScore(point)
                        .rating(score)
                        .build();
            }

        }
        return null;
    }


//    public GradeMngmnRes updGradeMngmn(GradeMngmnUpdDto dto) {
////        List<StudentSemesterScoreEntity> all = GM_REP.findAll();
////        for (StudentSemesterScoreEntity a : all) {
////            GradeUtils utils = new GradeUtils();
////            int avgScore = a.getAvgScore();
////            double v = utils.totalScore2(avgScore);
////            entity.setRating(v);
////        StudentSemesterScoreEntity save = GM_REP.save(entity);
//        return null;
//    }
//        return null;


    public GradeMngmnFindRes selGradeMngmn(GradeMngmnDto dto) {
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

        return GradeMngmnFindRes.builder()
                .voList(voList)
                .student(vo)
                .avgVo(avg)
                .paging(utils)
                .build();
    }

    public GradeMngmnDetailVo selStudentDetail(GradeMngmnDetailSelDto dto) {
        return MAPPER.selGradeFindStudentDetail(dto);
    }
}
