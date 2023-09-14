package com.green.smartgradever2.professor;
import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.semester.SemesterRepository;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    private final LectureNameRepository lectureNameRepository;
    private final SemesterRepository semesterRepository;

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
                .dayWeek(lecture.getLectureScheduleEntity().getDayWeek())
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

        ProfessorEntity entity = professorRepository.findById(dto.getIprofessor()).orElse(null);

        if (entity == null) {
            // 해당 ID의 교수를 찾을 수 없는 경우를 처리합니다.
            // 예를 들어 예외를 던지거나 적절한 응답을 반환할 수 있습니다.
            return null;
        }

        Page<LectureApplyEntity> lecturePage = lectureApplyRepository.findByProfessorEntity(entity, pageable);

//        ProfessorEntity entity = professorRepository.findById(dto.getIprofessor()).get();
//        Page<LectureApplyEntity> lecturePage = lectureApplyRepository.findByProfessorEntity(entity,pageable);

        List<ProfessorSelAllDto> lectures = lecturePage.getContent().stream().filter(lectureEntity -> {
            if (dto.getYear() != 0 && dto.getLectureName() != null) {
                // year와 lectureName 파라미터가 모두 제공된 경우, 두 기준에 따라 필터링
                return lectureEntity.getSemesterEntity().getYear() == dto.getYear()
                        && lectureEntity.getLectureNameEntity().getLectureName().equals(dto.getLectureName());
            } else if (dto.getYear() != 0) {
                // year 파라미터만 제공된 경우, year에 따라 필터링
                return lectureEntity.getSemesterEntity().getYear() == dto.getYear();
            } else if (dto.getLectureName() != null) {
                // lectureName 파라미터만 제공된 경우, lectureName에 따라 필터링
                return lectureEntity.getLectureNameEntity().getLectureName().equals(dto.getLectureName());
            } else if (dto.getOpeningProcedures() != null) {

                return lectureEntity.getOpeningProceudres() == dto.getOpeningProcedures();

            } else {
                // year와 lectureName 파라미터가 모두 없는 경우, 모든 레코드 반환
                return true;
            }

        }).filter(pro -> pro.getOpeningProceudres() == 3 || pro.getOpeningProceudres() == 4)
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
                    Long studentCount = lectureStudentRep.countByLectureApplyEntity(lectureEntity);
                    lectureDto.setStudentCount(Math.toIntExact(studentCount));

                    return lectureDto;
                })
                .toList();

        // lectureApplyRepository에서 총 항목 수를 얻어옵니다.
        long totalItems = lecturePage.getTotalElements();

      // 페이지당 항목 수
        int pageSize = pageable.getPageSize();

      // 총 페이지 수 계산
        long maxPage = (totalItems + pageSize - 1) / pageSize;

        PagingUtils utils = new PagingUtils(pageable.getPageNumber(), (int)maxPage, 10);

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


