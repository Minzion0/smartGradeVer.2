package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessorGradeSevice {
    private final ProfessorGradeMapper mapper;

    public ProfessorGradeMngmnUpRes upMngnm(ProfessorGradeMngmnUpParam param, Long iprofessor, Long ilecture, Long ilectureStudent ) {

        ProfessorGradeMngmnUpDto dto = new ProfessorGradeMngmnUpDto();
        String msg="";
        ProfessorGradeMngmnUpRes res = new ProfessorGradeMngmnUpRes();


        dto.setIlectureStudent(ilectureStudent);
        dto.setIlecture(ilecture);
        dto.setIpofessor(iprofessor);
        dto.setFinishedYn(1);



        int attendance = param.getAttendance();
        int midtermExamination = param.getMidtermExamination();
        int finalExamination = param.getFinalExamination();


        // 각 점수들의 최대 값을 가져오기
        int maxAttendance = mapper.getMaxAttendance(ilecture);
        int maxMidtermExamination = mapper.getMaxMidtermExamination(ilecture);
        int maxFinalExamination = mapper.getMaxFinalExamination(ilecture);


        // 점수가 최대 값을 넘지 않도록 예외처리
        if (attendance > maxAttendance) {
            msg += "출석 점수가 최대값을 넘을 수 없습니다.";
            res.setMsg(msg);
            return res;
        }
        if (midtermExamination > maxMidtermExamination) {
            msg +="중간고사 점수가 최대값을 넘을 수 없습니다.";
            res.setMsg(msg);
            return res;
        }
        if (finalExamination > maxFinalExamination) {
            msg += "기말고사 점수가 최대값을 넘을 수 없습니다.";
            res.setMsg(msg);
            return res;
        }

        dto.setAttendance(attendance);
        dto.setMidtermExamination(midtermExamination);
        dto.setFinalExamination(finalExamination);

        int point = dto.getAttendance() + dto.getMidtermExamination() + dto.getFinalExamination();
        GradeUtils gradeUtils = new GradeUtils(point);
        double score = gradeUtils.totalScore();
        String rating = gradeUtils.totalRating(score);

        dto.setPoint(point);
        dto.setRating(rating);

        try {
            int result = mapper.upMngnm(dto);
            if (result == 1) {
                res = new ProfessorGradeMngmnUpRes(dto);
                res.setIpofessor(iprofessor);
                res.setIlecture(ilecture);
                return res;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return  null;
    }




    public ProfessorGradeMngmnSelRES selStudentScore (ProfessorGradeMngmnSelDto dto) {
        int maxPage = mapper.selStudentScoreCount(dto);
        PagingUtils utils = new PagingUtils(dto.getPage(), maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<ProfessorGradeMngmnSelVo> list = mapper.selStudentScore(dto);
        int point;
        double score;
        String rating;
        for (ProfessorGradeMngmnSelVo vo : list) {
            point = vo.getAttendance() + vo.getMidtermExamination() + vo.getFinalExamination();
            GradeUtils gradeUtils = new GradeUtils(point);
            score =  gradeUtils.totalScore();
            rating = gradeUtils.totalRating(score);
            vo.setGrade(rating);
            vo.setPoint(score);
        }
        return ProfessorGradeMngmnSelRES .builder()
                .list(list)
                .page(utils)
                .build();
    }

}
