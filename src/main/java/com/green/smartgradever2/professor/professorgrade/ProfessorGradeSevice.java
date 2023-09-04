package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.config.entity.SemesterEntity;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.professor.ProfessorRepository;
import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.utils.GradeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessorGradeSevice {
    private final ProfessorGradeMapper mapper;

    private final LectureApplyRepository lectureApplyRep;
    private final LectureStudentRepository lectureStudentRep;
    private final ProfessorRepository professerRep;



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

    // 교수 학생 성적 리스트
    public ProfessorGradeStudentDto getProGraStu(Long iprofessor, Long isemester) {
        Optional<ProfessorEntity> professorOptional = professerRep.findById(iprofessor);
        if (!professorOptional.isPresent()) {
            throw new EntityNotFoundException("교수를 찾을 수 없습니다: " + iprofessor);
        }
        ProfessorEntity professor = professorOptional.get();

        List<ProStudentLectureDto> studentLectures = new ArrayList<>();

        //List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntityIprofessor(iprofessor);

        List<Double> ratings = new ArrayList<>();
        List<String> grades = new ArrayList<>();
        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setIprofessor(iprofessor);

        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setIsemester(isemester);
        List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntityAndSemesterEntity(professorEntity, semesterEntity);

        for (LectureApplyEntity lecture : professorLectures) {
            List<LectureStudentEntity> attendedLectureEntities = lectureStudentRep.findByLectureApplyEntity(lecture);

            for (LectureStudentEntity lectureStudentEntity : attendedLectureEntities) {
                if (lectureStudentEntity.getFinishedYn() == 1) {
                    ProStudentLectureDto studentLectureDto = new ProStudentLectureDto();
                    studentLectureDto.setIlectureStudent(lectureStudentEntity.getIlectureStudent());
                    studentLectureDto.setIlecture(lectureStudentEntity.getLectureApplyEntity().getIlecture());
                    studentLectureDto.setAttendance(lectureStudentEntity.getAttendance());
                    studentLectureDto.setMidtermExamination(lectureStudentEntity.getMidtermExamination());
                    studentLectureDto.setFinalExamination(lectureStudentEntity.getFinalExamination());
                    studentLectureDto.setDayWeek(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getDayWeek());
                    studentLectureDto.setLectureStrTime(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getLectureStrTime());
                    studentLectureDto.setLectureEndTime(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getLectureEndTime());
                    studentLectureDto.setSudentNum(lectureStudentEntity.getStudentEntity().getStudentNum());
                    int totalScore = lectureStudentEntity.getAttendance() + lectureStudentEntity.getMidtermExamination() + lectureStudentEntity.getFinalExamination();
                    GradeUtils gradeUtils = new GradeUtils(totalScore);
                    double rating = gradeUtils.totalScore();
                    String grade = gradeUtils.totalRating(rating);

                    studentLectureDto.setTotalScore(totalScore);
                    studentLectureDto.setGrade(grade);
                    studentLectureDto.setRating(rating);

                    studentLectures.add(studentLectureDto);
                }
            }
        }

        double averageRating = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        String averageGrade = new GradeUtils().totalRating(averageRating);

        ProfessorGradeStudentDto professorGradeStudentDto = new ProfessorGradeStudentDto();
        professorGradeStudentDto.setIprofessor(iprofessor);
        professorGradeStudentDto.setLectureList(studentLectures);


        return professorGradeStudentDto;
    }


    public List<ProStudentLectureDto> getProList(Long iprofessor,Long isemester) {
        Optional<ProfessorEntity> professorOptional = professerRep.findById(iprofessor);
        if (!professorOptional.isPresent()) {
            throw new EntityNotFoundException("교수를 찾을 수 없습니다: " + iprofessor);
        }

        ProfessorEntity professor = professorOptional.get();

        // 해당 교수의 강의 목록 가져오기
//        List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntity(professor);
        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setIprofessor(iprofessor);

        SemesterEntity semesterEntity = new SemesterEntity();
        semesterEntity.setIsemester(isemester);
        List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntityAndSemesterEntity(professorEntity, semesterEntity);
        List<ProStudentLectureDto> studentLectures = new ArrayList<>();

        // 각 강의에 대한 학생 정보 추출
        for (LectureApplyEntity lecture : professorLectures) {
            List<LectureStudentEntity> attendedLectureEntities = lectureStudentRep.findByLectureApplyEntity(lecture);

            for (LectureStudentEntity lectureStudentEntity : attendedLectureEntities) {
                if (lectureStudentEntity.getFinishedYn() == 0) {
                    ProStudentLectureDto studentLectureDto = new ProStudentLectureDto();
                    studentLectureDto.setIlectureStudent(lectureStudentEntity.getIlectureStudent());


                    studentLectureDto.setIlecture(lecture.getIlecture());
                    studentLectureDto.setDayWeek(lecture.getLectureScheduleEntity().getDayWeek());
                    studentLectureDto.setLectureStrTime(lecture.getLectureScheduleEntity().getLectureStrTime());
                    studentLectureDto.setLectureEndTime(lecture.getLectureScheduleEntity().getLectureEndTime());
                    studentLectureDto.setAttendance(lectureStudentEntity.getAttendance());
                    studentLectureDto.setMidtermExamination(lectureStudentEntity.getMidtermExamination());
                    studentLectureDto.setFinalExamination(lectureStudentEntity.getFinalExamination());
                    studentLectureDto.setSudentNum(lectureStudentEntity.getStudentEntity().getStudentNum());

                    studentLectures.add(studentLectureDto);
                }
            }
        }

        return studentLectures;

    }

    public void updateObjection(Long ilecture, Long ilectureStudent, int newObjection) {
        newObjection = 2;
        LectureStudentEntity lectureStudentEntity = lectureStudentRep.findByLectureApplyEntityIlectureAndIlectureStudent(ilecture, ilectureStudent);
        if (lectureStudentEntity != null) {
            lectureStudentEntity.setObjection(newObjection);
            lectureStudentRep.save(lectureStudentEntity);
        } else {
            throw new IllegalArgumentException("강의 학생을 찾을 수 없습니다.");
        }
    }

}
