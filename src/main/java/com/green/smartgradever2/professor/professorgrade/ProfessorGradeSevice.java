package com.green.smartgradever2.professor.professorgrade;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.professor.ProfessorRepository;
import com.green.smartgradever2.professor.professorgrade.model.*;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfessorGradeSevice {
    private final ProfessorGradeMapper mapper;

    private final LectureApplyRepository lectureApplyRep;
    private final LectureStudentRepository lectureStudentRep;
    private final ProfessorRepository professerRep;
    private final ProfessorGradeQdsl professorGradeQdsl;



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
            existingGrade.setFinishedYn(1);
            existingGrade.setUpdatedAt(LocalDateTime.now());
            // 현재 시간 가져오기
            LocalDateTime now = LocalDateTime.now();
            existingGrade.setUpdatedAt(now);


            LocalDateTime correctionAt = now.plusWeeks(2);
            existingGrade.setCorrectionAt(LocalDate.from(correctionAt));
            existingGrade.setFinishedAt(lectureApply.getSemesterEntity().getSemesterEndDate());



            int totalScore = updatedAttendance + updatedMidterm + updatedFinalExam;

            // 등급 계산
            GradeUtils gradeUtils = new GradeUtils(totalScore);
            double rating = gradeUtils.totalScore();
            String grade = gradeUtils.totalStrRating(rating);

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
    public ProfessorGradeStudentRes getProGraStu(Long iprofessor, Long ilecture, Pageable page) {

        PagingUtils paging = new PagingUtils(page.getPageNumber(), page.getPageSize());

        Optional<ProfessorEntity> professorOptional = professerRep.findById(iprofessor);
        if (!professorOptional.isPresent()) {
            throw new EntityNotFoundException("교수를 찾을 수 없습니다: " + iprofessor);
        }
        ProfessorEntity professor = professorOptional.get();

        List<ProStudentLectureVo> studentLectures = new ArrayList<>();

        //List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntityIprofessor(iprofessor);

        List<Double> ratings = new ArrayList<>();
        List<String> grades = new ArrayList<>();
        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setIprofessor(iprofessor);


        Page<LectureApplyEntity> professorLectures= null;
        if (ilecture == null) {
            professorLectures = lectureApplyRep.findAllByProfessorEntity(professorEntity,page);
        }else{
         professorLectures = lectureApplyRep.findAllByProfessorEntityAndIlecture(professorEntity,ilecture,page);}


        for (LectureApplyEntity lecture : professorLectures) {
            List<LectureStudentEntity> attendedLectureEntities = lectureStudentRep.findByLectureApplyEntity(lecture);

            for (LectureStudentEntity lectureStudentEntity : attendedLectureEntities) {

                    ProStudentLectureVo studentLectureDto = new ProStudentLectureVo();
                    studentLectureDto.setIlectureStudent(lectureStudentEntity.getIlectureStudent());
                    studentLectureDto.setIlecture(lectureStudentEntity.getLectureApplyEntity().getIlecture());
                    studentLectureDto.setAttendance(lectureStudentEntity.getAttendance());
                    studentLectureDto.setMidtermExamination(lectureStudentEntity.getMidtermExamination());
                    studentLectureDto.setFinalExamination(lectureStudentEntity.getFinalExamination());
                    studentLectureDto.setDayWeek(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getDayWeek());
                    studentLectureDto.setStudentName(lectureStudentEntity.getStudentEntity().getNm());
                    studentLectureDto.setMajorName(lectureStudentEntity.getStudentEntity().getMajorEntity().getMajorName());
                    studentLectureDto.setStudentNum(lectureStudentEntity.getStudentEntity().getStudentNum());
                    int totalScore = lectureStudentEntity.getAttendance() + lectureStudentEntity.getMidtermExamination() + lectureStudentEntity.getFinalExamination();
                    GradeUtils gradeUtils = new GradeUtils(totalScore);
                    double rating = gradeUtils.totalScore();
                    String grade = gradeUtils.totalStrRating(rating);

                    studentLectureDto.setTotalScore(totalScore);
                    studentLectureDto.setGrade(grade);
                    studentLectureDto.setRating(String.valueOf(rating));

                    studentLectures.add(studentLectureDto);

                    ratings.add(rating);
                    grades.add(grade);


                }
            }


        double averageRating = ratings.isEmpty() ? 0.0 : ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        String averageGrade = grades.isEmpty() ? "" : new GradeUtils().totalStrRating(averageRating);

        paging.makePage(page.getPageNumber(), professorLectures.getTotalPages());

        ProfessorGradeStudentRes professorGradeStudentDto = ProfessorGradeStudentRes.builder()
                .iprofessor(iprofessor)
                .page(paging)
                .lectureList(studentLectures)
                .build();


        return professorGradeStudentDto;
    }


//    public ProfessorStuLectureRes getProList(Long iprofessor, Long ilecture, Pageable page) {
//
//        PagingUtils paging = new PagingUtils(page.getPageNumber(), page.getPageSize());
//
//
//        Optional<ProfessorEntity> professorOptional = professerRep.findById(iprofessor);
//        if (!professorOptional.isPresent()) {
//            throw new EntityNotFoundException("교수를 찾을 수 없습니다: " + iprofessor);
//        }
//
//        ProfessorEntity professor = professorOptional.get();
//
//        // 해당 교수의 강의 목록 가져오기
////        List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntity(professor);
//        ProfessorEntity professorEntity = new ProfessorEntity();
//        professorEntity.setIprofessor(iprofessor);
//
//
//
//        List<LectureApplyEntity> professorLectures = null;
//        if (ilecture == null) {
//            professorLectures=  lectureApplyRep.findAllByProfessorEntity(professorEntity, page);
//
//        } else {
//            professorLectures = lectureApplyRep.findAllByProfessorEntityAndIlecture(professorEntity, ilecture, page);
//        }
//
//
//        List<ProfessorStudentLectureDto> studentLectures = new ArrayList<>();
//
//        // 각 강의에 대한 학생 정보 추출
//        for (LectureApplyEntity lecture : professorLectures) {
//            List<LectureStudentEntity> attendedLectureEntities = lectureStudentRep.findByLectureApplyEntity(lecture);
//
//            for (LectureStudentEntity lectureStudentEntity : attendedLectureEntities) {
//                if (lectureStudentEntity.getFinishedYn() == 0) {
//                    ProfessorStudentLectureDto studentLectureDto = new ProfessorStudentLectureDto();
//                    studentLectureDto.setIlectureStudent(lectureStudentEntity.getIlectureStudent());
//
//
//                    studentLectureDto.setIlecture(lecture.getIlecture());
//                    studentLectureDto.setDayWeek(lecture.getLectureScheduleEntity().getDayWeek());
//                    studentLectureDto.setLectureStrTime(lecture.getLectureScheduleEntity().getLectureStrTime());
//                    studentLectureDto.setLectureEndTime(lecture.getLectureScheduleEntity().getLectureEndTime());
//                    studentLectureDto.setAttendance(lectureStudentEntity.getAttendance());
//                    studentLectureDto.setMidtermExamination(lectureStudentEntity.getMidtermExamination());
//                    studentLectureDto.setFinalExamination(lectureStudentEntity.getFinalExamination());
//                    studentLectureDto.setSudentNum(lectureStudentEntity.getStudentEntity().getStudentNum());
//                    studentLectureDto.setPhone(lectureStudentEntity.getStudentEntity().getPhone());
//                    studentLectureDto.setGender(lectureStudentEntity.getStudentEntity().getGender());
//                    studentLectureDto.setStudentName(lectureStudentEntity.getStudentEntity().getNm());
//                    studentLectureDto.setMajorName(lectureStudentEntity.getStudentEntity().getMajorEntity().getMajorName());
//
//                    studentLectures.add(studentLectureDto);
//                }
//            }
//        }
//        paging.makePage(page.getPageNumber(), professorLectures.size());
//
//        ProfessorStuLectureRes professorStuLectureRes = ProfessorStuLectureRes.builder()
//                .iprofessor(iprofessor)
//                .page(paging)
//                .lectureList(studentLectures)
//                .build();
//
//        return professorStuLectureRes;
//
//    }

    public void updateObjection(Long ilecture, Long ilectureStudent, int newObjection, Long iuser) {
        newObjection = 2;
        LectureStudentEntity lectureStudentEntity = lectureStudentRep.findByLectureApplyEntityIlectureAndIlectureStudent(ilecture, ilectureStudent);
        if (lectureStudentEntity != null) {
            lectureStudentEntity.setObjection(newObjection);
            lectureStudentRep.save(lectureStudentEntity);
        } else {
            throw new IllegalArgumentException("강의 학생을 찾을 수 없습니다.");
        }
    }

    public Map<String, Object> getProfessorGradeList(Long iprofessor, Long ilecture, Pageable page) {
        Map<String, Object> result = professorGradeQdsl.selProfessoreGradeList(iprofessor, ilecture, page);
        return result;
    }



    public ProfessorListStudentRes getProList(Long iprofessor, Long ilecture, int year, Long studentNum,String nm, Pageable pageable) {

        Optional<ProfessorEntity> professorOptional = professerRep.findById(iprofessor);
        if (!professorOptional.isPresent()) {
            throw new EntityNotFoundException("교수를 찾을 수 없습니다: " + iprofessor);
        }


        // 해당 교수의 강의 목록 가져오기
//        List<LectureApplyEntity> professorLectures = lectureApplyRep.findAllByProfessorEntity(professor,pageable);
        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setIprofessor(iprofessor);

        LectureStudentEntity lectureStudent = new LectureStudentEntity();
        lectureStudent.getLectureApplyEntity().getIlecture();

        Page<LectureApplyEntity> professorLectures = null;
        if (ilecture == null) {
            professorLectures=  lectureApplyRep.findAllByProfessorEntity(professorEntity, pageable);

        } else {
            professorLectures = lectureApplyRep.findAllByProfessorEntityAndLectureStudentEntity(professorEntity, lectureStudent, pageable);
        }
        List<ProStudentListVo> studentLectures = new ArrayList<>();
        PagingUtils paging = null;

        Page<LectureStudentEntity> list ;
        // 각 강의에 대한 학생 정보 추출
        for (LectureApplyEntity lecture : professorLectures) {
            Page<LectureStudentEntity> attendedLectureEntities = lectureStudentRep.findByLectureApplyEntity(lecture,pageable);

            for (LectureStudentEntity lectureStudentEntity : attendedLectureEntities) {
                if ((studentNum == null || lectureStudentEntity.getStudentEntity().getStudentNum().equals(studentNum))
                        && (nm == null || lectureStudentEntity.getStudentEntity().getNm().contains(nm))) {
                ProStudentListVo studentLectureDto = new ProStudentListVo();
                studentLectureDto.setIlectureStudent(lectureStudentEntity.getIlectureStudent());
                studentLectureDto.setIlecture(lecture.getIlecture());
                studentLectureDto.setStudentNum(lectureStudentEntity.getStudentEntity().getStudentNum());
                studentLectureDto.setPhone(lectureStudentEntity.getStudentEntity().getPhone());
                studentLectureDto.setGender(lectureStudentEntity.getStudentEntity().getGender());
                studentLectureDto.setStudentName(lectureStudentEntity.getStudentEntity().getNm());
                studentLectureDto.setMajorName(lectureStudentEntity.getStudentEntity().getMajorEntity().getMajorName());
                studentLectureDto.setGrade(lectureStudentEntity.getStudentEntity().getGrade());
                studentLectures.add(studentLectureDto);
//                    long Maxpage = lectureStudentRep.countByLectureApplyEntity(lecture);
//                   paging = new PagingUtils(pageable.getPageNumber() ,(int)Maxpage ,pageable.getPageSize());
            }

            }
        }
        Collections.sort(studentLectures, Comparator.comparing(ProStudentListVo::getIlectureStudent).reversed());
        // 페이징 처리

//        paging.makePage(studentLectures.size(), pageable.getPageNumber()+1 );

        return ProfessorListStudentRes.builder()
                .page(paging)
                .lecturelist(studentLectures)
                .build();
    }

    public  ProfessorGradeMngmnSelRES selStudentScore (ProfessorGradeMngmnSelDto dto) {
        int maxPage = mapper.selStudentScoreCount(dto);
        PagingUtils utils = new PagingUtils(dto.getPage(), maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<ProfessorGradeMngmnSelVo> list = mapper.selStudentScore(dto);

        return ProfessorGradeMngmnSelRES .builder()
                .list(list)
                .page(utils)
                .build();
    }

}
