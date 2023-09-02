package com.green.smartgradever2.lecture_apply;

import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.lectureroom.AdminLectureRoomRepository;
import com.green.smartgradever2.admin.professor.AdminProfessorRepository;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.model.*;
import com.green.smartgradever2.lectureschedule.LectureScheduleRepository;
import com.green.smartgradever2.professor.ProfesserRepository;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
    private final ProfesserRepository professerRep;






public LectureApplyRes InsApply(Long iprofessor, LectureAppllyInsParam param) throws Exception {

    SemesterEntity semesterEntity = getCurrentSemester();

    if (LocalDate.now().isBefore(semesterEntity.getLectureApplyDeadline())){
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
    String query = "SELECT ls from LectureScheduleEntity ls  where ls.lectureApplyEntity.lectureRoomEntity.ilectureRoom = :ilectureRoom and ls.dayWeek = :dayWeek";
    List<LectureScheduleEntity> lectureScheduleEntity = EM.createQuery(query).setParameter("ilectureRoom",param.getIlectureRoom()).setParameter("dayWeek",param.getDayWeek()).getResultList();

    for (LectureScheduleEntity schedule : lectureScheduleEntity) {
        LocalTime lectureStrTime = schedule.getLectureStrTime();
        LocalTime lectureEndTime = schedule.getLectureEndTime();

        // 강의 시간 중복 체크
       if ((strTime.isAfter(lectureStrTime) && strTime.isBefore(lectureEndTime)) || strTime.equals(lectureStrTime) ){
               throw new Exception("해당 시간에 강의중인 강의가 있습니다");
       }
       if (endTime.equals(lectureEndTime) || (strTime.isBefore(lectureStrTime) && endTime.isAfter(lectureEndTime)) || (strTime.isBefore(lectureStrTime) && endTime.isBefore(lectureEndTime) )  ) {
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

        if (param.getGradeLimit()<5){
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





    
    public LectureApplyScheduleRes lectureRoomSchedule(Long ilectureRoom){
        SemesterEntity currentSemester = getCurrentSemester();

        String query = "SELECT sh FROM LectureScheduleEntity sh INNER JOIN sh.lectureApplyEntity la " +
                "INNER JOIN la.lectureRoomEntity rm WHERE sh.semesterEntity = :semester AND rm.ilectureRoom = :ilectureRoom  ";

        List<LectureScheduleEntity> resultList = EM.createQuery(query).setParameter("semester", currentSemester).setParameter("ilectureRoom", ilectureRoom).getResultList();

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

    public String openApiTest(String book){
        String clientId = "6c1d_wx2wYAf73bPpAhj"; //애플리케이션 클라이언트 아이디
        String clientSecret = "Pe9NsHDIy6"; //애플리케이션 클라이언트 시크릿


        String text = null;
        try {
            text = URLEncoder.encode(book, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }


        String apiURL = "https://openapi.naver.com/v1/search/book.json?query=" + text;    // JSON 결과
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);


        System.out.println(responseBody);
        return clientId;
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }




    public LectureSelAllRes getList(Long iprofessor,Integer openingProceudres ) {
        ProfessorEntity professor = professerRep.findById(iprofessor).orElse(null);

        if (professor == null) {
            return null;
        }

        List<LectureApplyEntity> lectureApplyEntityList = null;
        if (openingProceudres != null) {
            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntityAndOpeningProceudres(professor, openingProceudres);
        }else if (openingProceudres == null) {
            lectureApplyEntityList = LECTURE_APPLY_RPS.findByProfessorEntity(professor);
        }

        List<LectureApplySelDto> seldto = new ArrayList<>();

        for (LectureApplyEntity lectureApplyEntity : lectureApplyEntityList) {
            LectureApplySelDto dto = new LectureApplySelDto();
            dto.setIlecture(lectureApplyEntity.getIlecture());
            dto.setOpeningProceudres(lectureApplyEntity.getOpeningProceudres());
            dto.setIlectureName(lectureApplyEntity.getLectureNameEntity().getIlectureName());
            dto.setLectureName(lectureApplyEntity.getLectureNameEntity().getLectureName());
            dto.setScore(lectureApplyEntity.getLectureNameEntity().getScore());
            dto.setIlectureRoom(lectureApplyEntity.getLectureRoomEntity().getIlectureRoom());
            dto.setIsemester(lectureApplyEntity.getSemesterEntity().getIsemester());
            dto.setAttendance(lectureApplyEntity.getAttendance());
            dto.setMidtermExamination(lectureApplyEntity.getMidtermExamination());
            dto.setFinalExamination(lectureApplyEntity.getFinalExamination());
            dto.setLectureStrTime(lectureApplyEntity.getLectureScheduleEntity().getLectureStrTime());
            dto.setLectureEndTime(lectureApplyEntity.getLectureScheduleEntity().getLectureEndTime());
            dto.setGradeLimit(lectureApplyEntity.getGradeLimit());
            dto.setLectureMaxPeople(lectureApplyEntity.getLectureMaxPeople());
            dto.setCtnt(lectureApplyEntity.getCtnt());
            dto.setBooUrl(lectureApplyEntity.getBookUrl());
            seldto.add(dto);
        }

        return LectureSelAllRes.builder()
                .lectureList(seldto)
                .build();
    }


}
