package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.lecture_apply.model.*;
import com.green.smartgradever2.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureApplyService {
    private final LectureApplyMapper mapper;

    public LectureAppllyRes InsApplly(LectureAppllyInsDto dto){

        String msg="";
        LectureAppllyRes res= new LectureAppllyRes();


        // 입력 값들을 가져온다
        int attendance = dto.getAttendance();
        int midtermExamination = dto.getMidtermExamination();
        int finalExamination = dto.getFinalExamination();


        // 출석, 중간고사, 기말고사 점수의 배점을 계산
        int totalScore = attendance + midtermExamination + finalExamination;


        if (totalScore > 100) {
            msg+= "출석, 중간고사, 기말고사 점수의 합은 100을 넘을 수 없습니다.";
            res.setMsg(msg);
            return res;
        } else if (totalScore < 100) {
            msg+="출석, 중간고사, 기말고사 점수의 합은 100미만 일수 없습니다.";
            res.setMsg(msg);
            return res;

        }

        dto.setAttendance(attendance);
        dto.setMidtermExamination(midtermExamination);
        dto.setFinalExamination(finalExamination);


        int garedLimit = dto.getGaredLimit();
        if (garedLimit < 1 || garedLimit > 5) {
            // garedLimit 값이 1부터 5 사이에 없는 경우, 기본값으로 1을 설정
            garedLimit = 1;
        }
        dto.setGaredLimit(garedLimit);

        int lectureMaxPeople = dto.getLectureMaxPeople();
        // lectureMaxPeople 값이 1부터 30 사이에 없는 경우, 기본값으로 10을 설정
        if (lectureMaxPeople < 1 || lectureMaxPeople >= 30) {
            lectureMaxPeople = 10;
        }
        dto.setLectureMaxPeople(lectureMaxPeople);

        int openingProcedures = dto.getOpeningProcedures();
        if (openingProcedures <= 0 || openingProcedures > 5) {
            openingProcedures = 1;
        }
        dto.setOpeningProcedures(openingProcedures);


        try {
            int result = mapper.InsApplly(dto);
            if (result != 1) {
                throw new RuntimeException();
            }
        } catch (IllegalArgumentException ex) {
            ex.fillInStackTrace();
        }

        String dayWeek = dto.getDayWeek();
        String[] split = dayWeek.split(",");

        List<LectureApllyDto> list = new ArrayList<>();
        for (String s : split) {
            LectureApllyDto applyDto = new LectureApllyDto();
            applyDto.setDayWeek(s);
            applyDto.setIlecture(dto.getIlecture());
            list.add(applyDto);
        }

        int re = mapper.InsDayWeek(list);
        if (re ==0){
            return null;
        }
        res.setDto(dto);

        return res;
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
