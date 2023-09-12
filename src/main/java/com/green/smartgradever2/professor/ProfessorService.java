package com.green.smartgradever2.professor;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.professor.model.*;
import com.green.smartgradever2.utils.FileUtils;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorMapper MAPPER;
    private final ProfessorRepository professorRepository;
    private final LectureApplyRepository lectureApplyRepository;
    private final PasswordEncoder PW_ENCODER;
    private final LectureStudentRepository lectureStudentRep;
    private final ProfessorQdsl professorQdsl;

    @Value("${file.dir}")
    private String fileDir;

    //교수 프로필 디테일
    public ProfessorSelRes getProfessorLectures(Long iprofessor) {
        ProfessorEntity professor = professorRepository.findById(iprofessor).get();
        ProfessorProfileDto dto = new ProfessorProfileDto();
        dto.setIprofessor(professor.getIprofessor());
        dto.setMajorName(professor.getMajorEntity().getMajorName());  // 전공 정보 추가
        dto.setName(professor.getNm());  // 이름 추가
        dto.setGender(professor.getGender());  // 성별 추가
        dto.setPic(professor.getPic());  // 사진 추가
        dto.setBirthdate(professor.getBirthDate());  // 생년월일 추가
        dto.setPhone(professor.getPhone());  // 폰번호 추가
        dto.setEmail(professor.getEmail());  // 이메일 추가
        dto.setAddress(professor.getAddress());  // 주소 추가
        dto.setCreatedAt(professor.getCreatedAt());
        dto.setDelYn(professor.getDelYn());

        dto.setSecretKey(professor.getSecretKey() == null ? "false" : "true");

        SemesterEntity semester = professorQdsl.findSemester();

        String deadline = semester.getLectureApplyDeadline().toString();




        List<LectureApplyEntity> lectureApplyEntityList = lectureApplyRepository.findByProfessorEntity(professor);

        List<ProfessorLectureVo> lectureList = lectureApplyEntityList.stream().filter(lecture -> lecture.getOpeningProceudres()==3).map(lecture -> ProfessorLectureVo.builder()
                .ilecture(lecture.getIlecture())
                .lectureStrTime(lecture.getLectureScheduleEntity().getLectureStrTime())
                .lectureEndTime(lecture.getLectureScheduleEntity().getLectureEndTime())
                .lectureStrDate(lecture.getSemesterEntity().getSemesterStrDate())
                .lectureEndDate(lecture.getSemesterEntity().getSemesterEndDate())
                .lectureName(lecture.getLectureNameEntity().getLectureName()).build()).toList();


        return ProfessorSelRes.builder().profile(dto).lectureList(lectureList).deadline(deadline).build();
    }

    @Transactional
    public ProfessorUpRes upProfessor(MultipartFile pic, ProfessorParam param,Long iprofessor) {
        ProfessorEntity professor = professorRepository.findByIprofessor(iprofessor);
        if (professor == null) {
            // 교수 정보가 없을 경우 처리
            return null;
        }
        professor.setAddress(param.getAddress());
        professor.setPhone(param.getPhone());
        professor.setEmail(param.getEmail());
        if (pic != null) {
            String centerPath = String.format("professor/%d", professor.getIprofessor());
            String dicPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), centerPath);
            String savedFileName = FileUtils.makeRandomFileNm(pic.getOriginalFilename());
            String savedFilePath = String.format("%s/%s", centerPath, savedFileName);
            String targetPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), savedFilePath);
            File dic = new File(dicPath);
            if (!dic.exists()) {
                dic.mkdirs();
            }
            File target = new File(targetPath);
            try {
                pic.transferTo(target);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패");
            }
            professor.setPic(savedFileName);
        }
        try {
            professorRepository.save(professor);
        } catch (Exception e) {
            // 업로드에 실패한 경우 파일 삭제
            if (pic != null) {
                String picFileName = professor.getPic();
                String picPath = String.format("professor/%d/%s", professor.getIprofessor(), picFileName);
                String fullPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), picPath);
                File picFile = new File(fullPath);
                if (picFile.exists()) {
                    picFile.delete();
                }
            }
            throw new RuntimeException("사진 불가");
        }
        return new ProfessorUpRes(professor);
    }
    @Transactional
    public void processProfessorPicDeletion(Long iprofessor) {
        ProfessorEntity professor = professorRepository.findByIprofessor(iprofessor);
        if (professor != null && professor.getPic() != null) {
            // 기존 사진이 있을 경우 삭제
            String picFileName = professor.getPic();
            String picPath = String.format("professor/%d/%s", iprofessor, picFileName);
            String fullPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), picPath);
            File picFile = new File(fullPath);
            if (picFile.exists()) {
                picFile.delete();
            }
            professor.setPic(null);
            try {
                professorRepository.save(professor);
            } catch (Exception e) {
                throw new RuntimeException("사진 삭제 실패");
            }
        }
    }

    //본인의 강의 조회
    public ProfessorSelLectureRes selProfessorLecture(ProfessorSelLectureDto dto,Pageable pageable) {
//        int itemsPerPage = 10; // 한 페이지당 수
//        Pageable pageable = PageRequest.of(dto.getPage() - 1, itemsPerPage);
//        Page<LectureApplyEntity> lecturePage = lectureApplyRepository.findAllByProfessorEntityIprofessor(dto.getIprofessor(), pageable);
//        List<ProfessorSelAllDto> lectures = new ArrayList<>();
//        for (LectureApplyEntity lectureEntity : lecturePage.getContent()) {
//            ProfessorSelAllDto lectureDto = new ProfessorSelAllDto();
//            lectureDto.setIlecture(lectureEntity.getIlecture());
//            lectureDto.setLectureName(lectureEntity.getLectureNameEntity().getLectureName());
//            lectureDto.setLectureRoomName(lectureEntity.getLectureRoomEntity().getLectureRoomName());
//            lectureDto.setIsemester(lectureEntity.getSemesterEntity().getIsemester());
//            lectureDto.setOpeningProceudres(lectureEntity.getOpeningProceudres());
//            lectureDto.setGradeLimit(lectureEntity.getGradeLimit());
//            lectureDto.setLectureMaxPeople(lectureEntity.getLectureMaxPeople());
//            lectureDto.setScore(lectureEntity.getLectureNameEntity().getScore());
//            lectureDto.setDelYn(lectureEntity.getDelYn());
//            lectureDto.setYear(lectureEntity.getSemesterEntity().getYear());
//            lectures.add(lectureDto);
//        }

        ProfessorEntity entity = professorRepository.findById(dto.getIprofessor()).get();
        Page<LectureApplyEntity> lecturePage = lectureApplyRepository.findByProfessorEntity(entity, pageable);

        List<ProfessorSelAllDto> lectures = lecturePage.getContent().stream()
                .map(lectureEntity -> {
                    ProfessorSelAllDto lectureDto = new ProfessorSelAllDto();
                    lectureDto.setIlecture(lectureEntity.getIlecture());
                    lectureDto.setLectureName(lectureEntity.getLectureNameEntity().getLectureName());
                    lectureDto.setBuildingName(lectureEntity.getLectureRoomEntity().getBuildingName());
                    lectureDto.setLectureStrTime(String.valueOf(lectureEntity.getLectureScheduleEntity().getLectureStrTime()));
                    lectureDto.setLectureEndTime(String.valueOf(lectureEntity.getLectureScheduleEntity().getLectureEndTime()));
                    lectureDto.setLectureRoomName(lectureEntity.getLectureRoomEntity().getLectureRoomName());
                    lectureDto.setIsemester(lectureEntity.getSemesterEntity().getIsemester());
                    lectureDto.setOpeningProceudres(lectureEntity.getOpeningProceudres());
                    lectureDto.setGradeLimit(lectureEntity.getGradeLimit());
                    lectureDto.setLectureMaxPeople(lectureEntity.getLectureMaxPeople());
                    lectureDto.setScore(lectureEntity.getLectureNameEntity().getScore());
                    lectureDto.setDelYn(lectureEntity.getDelYn());
                    lectureDto.setDayWeek(lectureEntity.getLectureScheduleEntity().getDayWeek());
                    lectureDto.setYear(lectureEntity.getSemesterEntity().getYear());
                    lectureDto.setCtnt(lectureEntity.getCtnt());
                    lectureDto.setTextbook(lectureEntity.getTextbook());
                    lectureDto.setBookUrl(lectureEntity.getBookUrl());
                    int studentCount = lectureStudentRep.countByLectureApplyEntity(lectureEntity);
                    lectureDto.setStudentCount(studentCount);

                    return lectureDto;
                })
                .toList();

        long maxPage =lectureApplyRepository.count();
        PagingUtils utils = new PagingUtils(pageable.getPageNumber(), (int)maxPage,10);

        return ProfessorSelLectureRes.builder()
                .page(utils)
                .lectureList(lectures)
                .build();
    }

    /** 교수 비밀번호 변경 (현재 비밀번호 확인 후 변경 가능(로그인 완료시 가능)) **/
    public String updPassword(ProfessorUpdPasswordDto updDto, ProfessorUpdPasswordParam param) throws Exception {
        ProfessorSelCurrentPasswordDto dto = new ProfessorSelCurrentPasswordDto();
        dto.setIprofessor(updDto.getIprofessor());
        dto.setRole(updDto.getRole());

        ProfessorEntity entity = professorRepository.findById(dto.getIprofessor()).get();

        if (!PW_ENCODER.matches(param.getCurrentProfessorPassword(), entity.getProfessorPassword())){
            throw new Exception("기존 비밀번호를 다시 확인해주세요");
        }
        updDto.setProfessorPassword(param.getProfessorPassword());
        String npw = PW_ENCODER.encode(updDto.getProfessorPassword());
        entity.setProfessorPassword(npw);
        professorRepository.save(entity);

        return "비밀번호 변경이 완료 되었습니다.";
    }


//    public List<ProfessorStudentData> getStudentsWithObjectionAndScores(Long ilecture, int objection,Long professorId) {
//        List<LectureStudentEntity> studentsData = lectureStudentRep.findByLectureApplyEntityIlectureAndObjection(ilecture, objection);
//
//        List<ProfessorStudentData> professorStudentDataList = new ArrayList<>();
//
//        for (LectureStudentEntity entity : studentsData) {
//            GradeUtils gradeUtils = new GradeUtils();
//            ProfessorStudentData professorStudentData = new ProfessorStudentData();
//            professorStudentData.setStudentNum(entity.getStudentEntity().getStudentNum());
//            professorStudentData.setStudentName(entity.getStudentEntity().getNm());
//            professorStudentData.setTotalScore(entity.getTotalScore());
//            professorStudentData.setIlectureStudent(entity.getIlectureStudent());
//            professorStudentData.setMajorName(entity.getStudentEntity().getMajorEntity().getMajorName());
//
//            String grade = gradeUtils.totalGradeFromScore1(professorStudentData.getTotalScore());
//            professorStudentData.setGrade(grade);
//
//            if (objection == 1) {
//                professorStudentDataList.add(professorStudentData);
//            }
//        }
//
//        return professorStudentDataList;
//    }
//
    public List<ProfessorScheduleRes> professorScheduleList(Long iprofessor){

        List<ProfessorScheduleVo> professorScheduleVos = professorQdsl.professorScheduleList(iprofessor);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
       return professorScheduleVos.stream().map(list-> ProfessorScheduleRes.builder()
                .dayWeek(list.getDayWeek())
                .startTime(list.getStartTime().format(formatter))
                .endTime(list.getEndTime().format(formatter))
                .lectureName(list.getLectureName())
               .lectureRoomName(list.getBuildingName()+" "+list.getLectureRoomName())
                .build()).toList();



    }


    public List<ProfessorStudentData> getStudentObjection(Long ilecture,Long ifrofessor , Pageable pageable) {
        List<ProfessorStudentData> studentLectureVos = professorQdsl.getStudentObjection(ilecture, ifrofessor, pageable);

        return studentLectureVos;
    }
}


