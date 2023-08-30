package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.lectureroom.AdminLectureRoomRepository;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.model.*;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureApplyService {
    private final LectureApplyMapper mapper;
    private final LectureApplyRepository LECTURE_APPLY_RPS;
    private final AdminLectureRoomRepository LECTURE_ROOM_RPS;
    private final SemesterRepository SEMESTER_RPS;
    private final LectureNameRepository LECTURE_NAME_RPS;






public LectureApplyRes InsApply(Long iprofessor, LectureAppllyInsParam param){


        int attendance = param.getAttendance();
        int midtermExamination = param.getMidtermExamination();
        int finalExamination = param.getFinalExamination();

        int totalScore = attendance + midtermExamination + finalExamination;

    ProfessorEntity professorEntity = new ProfessorEntity();
    professorEntity.setIprofessor(iprofessor);

    if (totalScore > 100) {
            new Exception("출석, 중간고사, 기말고사 점수의 합은 100을 넘을 수 없습니다.");

        } else if (totalScore < 100) {
           new Exception("출석, 중간고사, 기말고사 점수의 합은 100미만 일수 없습니다.") ;
        }

        List<SemesterEntity> semester = SEMESTER_RPS.findAll(Sort.by(Sort.Direction.DESC, "isemester"));
        SemesterEntity semesterEntity = semester.get(0);

        LectureRoomEntity lectureRoomEntity = LECTURE_ROOM_RPS.findById(param.getIlectureRoom()).get();

        Optional<LectureNameEntity> byId = LECTURE_NAME_RPS.findById(param.getIlectureName());

        if (byId.isEmpty()){
            new Exception("해당 강의명이 없습니다");
        }

        if (lectureRoomEntity.getMaxCapacity()>param.getLectureMaxPeople()){
            new Exception("강의실 정원 초과입니다");
        }

        if (param.getGradeLimit()<5){
            new Exception("학년은 1~4학년 까지 입니다");
        }


        LectureApplyEntity lectureApplyEntity = LectureApplyEntity.builder()
                .lectureRoomEntity(lectureRoomEntity)
                .lectureNameEntity(byId.get())
                .semesterEntity(semesterEntity)
                .professorEntity(professorEntity)
                .attendance(attendance)
                .midtermExamination(midtermExamination)
                .finalExamination(finalExamination)
                .ctnt(param.getCtnt())
                .textbook(param.getTextBook())
                .bookUrl(param.getBookUrl())
                .gradeLimit(param.getGradeLimit())
                .lectureMaxPeople(param.getLectureMaxPeople())
                .build();

        LECTURE_APPLY_RPS.save(lectureApplyEntity);


//    LectureApplyRes.builder()
//            .ilecture(lectureApplyEntity.getIlecture())
//            .
//            .build();



        return null ;
    }


    public LectureApllySelRes selLectureApplly(int page, Long ip) {
        int maxPage = mapper.selAplly();
        PagingUtils utils = new PagingUtils(page,maxPage);

        LectureApllyT t = new LectureApllyT();
        t.setRow(utils.getROW());
        t.setStartIdx(utils.getStaIdx());
        t.setIprofessor(ip);

        List<LectureAppllySelOneRes> applly = mapper.selLectureApplly(t);

        return LectureApllySelRes.builder().list(applly).page(utils).build();
    }







}
