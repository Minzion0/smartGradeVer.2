package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessorGradeSevice {
    private final ProfessorGradeMapper mapper;

    private final LectureApplyRepository lectureApplyRep;
    private final LectureStudentRepository lectureStudentRep;



    public StudentGradeDTO updateStudentGrade(Long ilectureStudent, Long iprofessor, Long ilecture, ProfessorGradeDto updatedGrade) {
        LectureApplyEntity lectureApply = lectureApplyRep.findByProfessorEntityIprofessorAndIlecture(iprofessor, ilecture)
                .orElseThrow(() -> new EntityNotFoundException("강의 정보를 찾을 수 없습니다."));


        LectureStudentEntity existingGrade = lectureStudentRep.findById(ilectureStudent)
                .orElseThrow(() -> new EntityNotFoundException("학생 성적 정보를 찾을 수 없습니다."));

        int updatedAttendance = updatedGrade.getAttendance();
        int updatedMidterm = updatedGrade.getMidtermExamination();
        int updatedFinalExam = updatedGrade.getFinalExamination();

        int maxAttendance = lectureApply.getAttendance();
        int maxMidterm = lectureApply.getMidtermExamination();
        int maxFinalExam = lectureApply.getFinalExamination();

        if (updatedAttendance <= maxAttendance && updatedMidterm <= maxMidterm && updatedFinalExam <= maxFinalExam) {
            // 성적 정보 업데이트 및 저장
            existingGrade.setAttendance(updatedAttendance);
            existingGrade.setMidtermExamination(updatedMidterm);
            existingGrade.setFinalExamination(updatedFinalExam);
            int totalScore = updatedAttendance + updatedMidterm + updatedFinalExam;

            // 등급 계산
            GradeUtils gradeUtils = new GradeUtils(totalScore);
            double rating = gradeUtils.totalScore();
            String grade = gradeUtils.totalRating(rating);

            String ratingString = gradeUtils.totalGradeFromScore(totalScore);

            // 성적 정보 업데이트 및 저장
            existingGrade.setTotalScore(totalScore);
            lectureStudentRep.save(existingGrade);

            // DTO를 생성하여 반환
            StudentGradeDTO studentGradeDTO = new StudentGradeDTO();
            studentGradeDTO.setAttendance(updatedAttendance);
            studentGradeDTO.setMidtermExamination(updatedMidterm);
            studentGradeDTO.setFinalExamination(updatedFinalExam);
            studentGradeDTO.setTotalScore(totalScore);
            studentGradeDTO.setGrade(grade);
            studentGradeDTO.setRating(ratingString);

            return studentGradeDTO;
        } else {
            throw new GradeExceedsMaxScoreException("성적 정보의 배점이 초과되었습니다.");
        }
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
