package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.lectureroom.AdminLectureRoomRepository;
import com.green.smartgradever2.admin.professor.AdminProfessorRepository;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.model.*;
import com.green.smartgradever2.lectureschedule.LectureScheduleRepository;
import com.green.smartgradever2.professor.ProfessorRepository;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureApplyService {
    private final LectureApplyMapper mapper;
    private final LectureApplyRepository LECTURE_APPLY_RPS;
    private final AdminLectureRoomRepository LECTURE_ROOM_RPS;
    private final SemesterRepository SEMESTER_RPS;
    private final LectureNameRepository LECTURE_NAME_RPS;
    private final AdminProfessorRepository PROFESSOR_RPS;
    private final LectureScheduleRepository LECTURE_SCHEDULE_RPS;
    private final EntityManager EM;
    private final ProfessorRepository professerRep;
    private final LectureApplyQdsl lectureApplyQdsl;






public LectureApplyRes InsApply(Long iprofessor, LectureAppllyInsParam param) throws Exception {

    SemesterEntity semesterEntity = getCurrentSemester();

    if (LocalDate.now().isAfter(semesterEntity.getLectureApplyDeadline())){
            throw new Exception("강의 등록기간이 아님니다");
        }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    LocalTime strTime = LocalTime.parse(param.getLectureStrTime(), dateTimeFormatter);
    LocalTime endTime = LocalTime.parse(param.getLectureEndTime(), dateTimeFormatter);

    //강의 최소 최대 시간 확인
    LocalTime time = endTime.minusHours(strTime.getHour());
    if (time.getHour() ==0 || time.getHour() >3){
        throw new Exception("강의 시간은 최소 1 시간에서 최대 3시간 입니다");
    }

//  지금 학기의 요일 강의실 시간정보 호출
//    String query = "SELECT ls from LectureScheduleEntity ls  where ls.lectureApplyEntity.lectureRoomEntity.ilectureRoom = :ilectureRoom and ls.dayWeek = :dayWeek";
//    List<LectureScheduleEntity> lectureScheduleEntity = EM.createQuery(query).setParameter("ilectureRoom",param.getIlectureRoom()).setParameter("dayWeek",param.getDayWeek()).getResultList();
    List<LectureScheduleEntity> lectureSchedule = lectureApplyQdsl.findLectureSchedule(param);
    for (LectureScheduleEntity schedule : lectureSchedule) {
        LocalTime lectureStrTime = schedule.getLectureStrTime();
        LocalTime lectureEndTime = schedule.getLectureEndTime();

        // 강의 시간 중복 체크
       if ((strTime.isAfter(lectureStrTime) && strTime.isBefore(lectureEndTime)) || strTime.equals(lectureStrTime) ){
               throw new Exception("해당 시간에 강의중인 강의가 있습니다");
       }
       if (endTime.equals(lectureEndTime) || (strTime.isBefore(lectureStrTime) && endTime.isAfter(lectureEndTime)) || (strTime.isBefore(lectureStrTime) && endTime.isAfter(lectureEndTime) )  ) {
               throw new Exception("해당 시간에 강의중인 강의가 있습니다");
       }
    }

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


        LectureRoomEntity lectureRoomEntity = LECTURE_ROOM_RPS.findById(param.getIlectureRoom()).get();

        if (lectureRoomEntity.getMaxCapacity()>param.getLectureMaxPeople()){
            new Exception("강의실 정원 초과입니다");
        }

        if (param.getGradeLimit()>4){
            new Exception("학년은 1~4학년 까지 입니다");
        }

    LectureNameEntity lectureNameEntity = new LectureNameEntity();
        lectureNameEntity.setLectureName(param.getLectureName());
        lectureNameEntity.setScore(param.getScore());

        LECTURE_NAME_RPS.save(lectureNameEntity);


    LectureApplyEntity lectureApplyEntity = LectureApplyEntity.builder()
                .lectureRoomEntity(lectureRoomEntity)
                .lectureNameEntity(lectureNameEntity)
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



    LectureScheduleEntity scheduleEntity = new LectureScheduleEntity();
        scheduleEntity.setLectureApplyEntity(lectureApplyEntity);
        scheduleEntity.setSemesterEntity(semesterEntity);
        scheduleEntity.setDayWeek(param.getDayWeek());
        scheduleEntity.setLectureStrTime(strTime);
        scheduleEntity.setLectureEndTime(endTime);

    LECTURE_SCHEDULE_RPS.save(scheduleEntity);



   return LectureApplyRes.builder()
            .ilecture(lectureApplyEntity.getIlecture())
            .ilectureName(lectureApplyEntity.getLectureNameEntity().getIlectureName())
            .lectureName(lectureNameEntity.getLectureName())
            .score(lectureNameEntity.getScore())
            .ilectureRoom(lectureApplyEntity.getLectureRoomEntity().getIlectureRoom())
            .iprofessor(lectureApplyEntity.getProfessorEntity().getIprofessor())
            .isemester(lectureApplyEntity.getSemesterEntity().getIsemester())
            .lectureStrTime(scheduleEntity.getLectureStrTime().toString())
            .lectureEndTime(scheduleEntity.getLectureEndTime().toString())
            .attendance(lectureApplyEntity.getAttendance())
            .midtermExamination(lectureApplyEntity.getMidtermExamination())
            .finalExamination(lectureApplyEntity.getFinalExamination())
            .lectureMaxPeople(lectureApplyEntity.getLectureMaxPeople())
            .gradeLimit(lectureApplyEntity.getGradeLimit())
            .dayWeek(scheduleEntity.getDayWeek())
            .delYn(lectureApplyEntity.getDelYn())
            .build();

    }

    private SemesterEntity getCurrentSemester() {
        List<SemesterEntity> semester = SEMESTER_RPS.findAll(Sort.by(Sort.Direction.DESC, "isemester"));
        SemesterEntity semesterEntity = semester.get(0);
        return semesterEntity;
    }





    /**선택한 강의실의 시간표를 불러오는 메소드 **/
    public LectureApplyScheduleRes lectureRoomSchedule(Long ilectureRoom){
        SemesterEntity currentSemester = getCurrentSemester();

        List<LectureScheduleEntity> resultList = lectureApplyQdsl.findLectureRoomSchedule(ilectureRoom);

        LectureRoomEntity lectureRoomEntity= null;

        if (resultList.size()==0){
           lectureRoomEntity = LECTURE_ROOM_RPS.findById(ilectureRoom).get();
        }
        if (resultList.size()!=0){

            lectureRoomEntity = resultList.get(0).getLectureApplyEntity().getLectureRoomEntity();
        }


        List<LectureApplyScheduleVo> list=new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (LectureScheduleEntity scheduleEntity : resultList) {
            LocalTime lectureStrTime = scheduleEntity.getLectureStrTime();
            LocalTime lectureEndTime = scheduleEntity.getLectureEndTime();
            int dayWeek = scheduleEntity.getDayWeek();

            String start = lectureStrTime.format(formatter);
            String end = lectureEndTime.format(formatter);

            LectureApplyScheduleVo vo = new LectureApplyScheduleVo();
            vo.setStartTime(start);
            vo.setEndTime(end);
            vo.setDayWeek(dayWeek);
            list.add(vo);
        }


       return LectureApplyScheduleRes.builder()
                .ilectureRoom(lectureRoomEntity.getIlectureRoom())
                .buildingName(lectureRoomEntity.getBuildingName())
                .lectureRoomName(lectureRoomEntity.getLectureRoomName())
                .schedule(list)
                .build();


    }



    public LectureSelAllRes getList(Long iprofessor,String LectureName, Pageable page, Integer openingProceudres) {
        ProfessorEntity professor = professerRep.findById(iprofessor).orElse(null);

        if (professor == null) {
            return null;
        }
        LectureApplySelDto dto1 = new LectureApplySelDto();

        List<LectureApplyEntity> lectureApplyEntityList = null;
        if (LectureName != null ) {
            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntityAndLectureNameEntityLectureNameBetween(professor, LectureName,page,1,2);
        } else if (openingProceudres == null) {
            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntityAndOpeningProceudresBetween(professor, page, 1, 2);
        } else if (openingProceudres == 1) {
            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntityAndOpeningProceudresBetween(professor, page, 1, openingProceudres);
        } else {
            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntityAndOpeningProceudresBetween(professor, page, 2, openingProceudres);
        }
//        } else if (dto1.getOpeningProcedures() == null) {
//            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntityBetween(professor, page,1,2).stream().toList();
//
//        }

        List<LectureApplySelDto> seldto = new ArrayList<>();

        for (LectureApplyEntity lectureApplyEntity : lectureApplyEntityList) {
            LectureApplySelDto dto = new LectureApplySelDto();
            dto.setIlecture(lectureApplyEntity.getIlecture());
            dto.setOpeningProcedures(lectureApplyEntity.getOpeningProceudres());
            dto.setIlectureName(lectureApplyEntity.getLectureNameEntity().getIlectureName());
            dto.setLectureName(lectureApplyEntity.getLectureNameEntity().getLectureName());
            dto.setScore(lectureApplyEntity.getLectureNameEntity().getScore());
            dto.setIlectureRoom(lectureApplyEntity.getLectureRoomEntity().getIlectureRoom());
            dto.setLectureRoomName(lectureApplyEntity.getLectureRoomEntity().getLectureRoomName());
            dto.setDayWeek(lectureApplyEntity.getLectureScheduleEntity().getDayWeek());
            dto.setIsemester(lectureApplyEntity.getSemesterEntity().getIsemester());
            dto.setAttendance(lectureApplyEntity.getAttendance());
            dto.setMidtermExamination(lectureApplyEntity.getMidtermExamination());
            dto.setFinalExamination(lectureApplyEntity.getFinalExamination());
            dto.setLectureStrTime(String.valueOf(lectureApplyEntity.getLectureScheduleEntity().getLectureStrTime()));
            dto.setLectureEndTime(String.valueOf(lectureApplyEntity.getLectureScheduleEntity().getLectureEndTime()));
            dto.setGradeLimit(lectureApplyEntity.getGradeLimit());
            dto.setLectureMaxPeople(lectureApplyEntity.getLectureMaxPeople());
            dto.setCtnt(lectureApplyEntity.getCtnt());
            dto.setTextbook(lectureApplyEntity.getTextbook());
            dto.setBuildingName(lectureApplyEntity.getLectureRoomEntity().getBuildingName());
            dto.setBookUrl(lectureApplyEntity.getBookUrl());

            long studentCount = LECTURE_APPLY_RPS.countStudentsByIlecture(lectureApplyEntity.getIlecture());
            dto.setStudentCount(studentCount);

            seldto.add(dto);
        }
        long totalItems = LECTURE_APPLY_RPS.count();
        int pageSize = page.getPageSize();
        long maxPage = (totalItems + pageSize - 1) / pageSize;

        PagingUtils utils = new PagingUtils(page.getPageNumber(), (int) maxPage, 10);
//        long totalItems = .getTotalElements();
//
//        // 페이지당 항목 수
//        int pageSize = page.getPageSize();
//
//        // 총 페이지 수 계산
//        long maxPage = (totalItems + pageSize - 1) / pageSize;
//
//        PagingUtils utils = new PagingUtils(page.getPageNumber(), (int)maxPage, page.getPageSize());


        return LectureSelAllRes.builder()
                .page(utils)
                .lectureList(seldto)
                .build();
    }


}
