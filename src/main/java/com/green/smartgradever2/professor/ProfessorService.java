package com.green.smartgradever2.professor;

import com.green.smartgradever2.entity.*;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.professor.model.ProfessorLectureDto;
import com.green.smartgradever2.professor.model.ProfessorProfileDto;
import com.green.smartgradever2.professor.model.ProfessorSelRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorMapper MAPPER;

    private final ProfesserRepository professorRepository;
    private final LectureApplyRepository lectureApplyRepository;



    //교수 프로필
    public ProfessorProfileDto getProfessorProfile(Long iprofessor) {
        ProfessorEntity professor = professorRepository.findById(iprofessor).orElse(null);

        if (professor == null) {
            return null;
        }

        ProfessorProfileDto dto = new ProfessorProfileDto();
        dto.setIprofessor(professor.getIprofessor());
        dto.setImajor(professor.getMajorEntity().getImajor());  // 전공 정보 추가
        dto.setNm(professor.getNm());  // 이름 추가
        dto.setGender(professor.getGender());  // 성별 추가
        dto.setPic(professor.getPic());  // 사진 추가
        dto.setBirthDate(professor.getBirthDate());  // 생년월일 추가
        dto.setPhone(professor.getPhone());  // 폰번호 추가
        dto.setEmail(professor.getEmail());  // 이메일 추가
        dto.setAddress(professor.getAddress());  // 주소 추가
        dto.setRole(professor.getRole());  // 롤값 추가

        return dto;
    }
    private ProfessorLectureDto convertToProfessorLectureDto(LectureApplyEntity lectureEntity) {
        ProfessorLectureDto lectureDto = professorLectureSchedule(lectureEntity);

        return lectureDto;
    }
    //교수 프로필 디테일
    public ProfessorSelRes getProfessorLectures(ProfessorProfileDto dto) {
        ProfessorEntity professor = professorRepository.findById(dto.getIprofessor()).orElse(null);


        dto.setIprofessor(professor.getIprofessor());
        dto.setImajor(professor.getMajorEntity().getImajor());  // 전공 정보 추가
        dto.setNm(professor.getNm());  // 이름 추가
        dto.setGender(professor.getGender());  // 성별 추가
        dto.setPic(professor.getPic());  // 사진 추가
        dto.setBirthDate(professor.getBirthDate());  // 생년월일 추가
        dto.setPhone(professor.getPhone());  // 폰번호 추가
        dto.setEmail(professor.getEmail());  // 이메일 추가
        dto.setAddress(professor.getAddress());  // 주소 추가
        dto.setRole(professor.getRole());  // 롤값 추가

        ProfessorEntity professorEntity = new ProfessorEntity();
        professorEntity.setIprofessor(dto.getIprofessor());

        List<LectureApplyEntity> lectureList = lectureApplyRepository.findByProfessorEntity(professorEntity);

        return ProfessorSelRes.builder()
                .professor(dto)
                .lectures(lectureList.stream().map(this::convertToProfessorLectureDto).collect(Collectors.toList()))
                .build();
    }

    //교수 강의 정보
    private static ProfessorLectureDto professorLectureSchedule(LectureApplyEntity lectureEntity) {
        ProfessorLectureDto lectureDto = new ProfessorLectureDto();
        LectureNameEntity lectureNameEntity = lectureEntity.getLectureNameEntity();
        LectureRoomEntity lectureRoomEntity = lectureEntity.getLectureRoomEntity();
        SemesterEntity semesterEntity = lectureEntity.getSemesterEntity();
        // lectureEntity의 필드들을 lectureDto로 복사하여 설정
        lectureDto.setIlecture(lectureEntity.getIlecture());
        lectureDto.setIlectureName(lectureNameEntity.getIlectureName());
        lectureDto.setIlectureRoom(lectureRoomEntity.getIlectureRoom());
        lectureDto.setIsemester(semesterEntity.getIsemester());
        lectureDto.setOpeningProceudres(lectureEntity.getOpeningProceudres());
        lectureDto.setGradeLimit(lectureEntity.getGradeLimit());
        lectureDto.setAttendance(lectureEntity.getAttendance());
        lectureDto.setMidtermExamination(lectureEntity.getMidtermExamination());
        lectureDto.setFinalExamination(lectureEntity.getFinalExamination());
        lectureDto.setLectureMaxPeople(lectureEntity.getLectureMaxPeople());
        lectureDto.setLectureEndDate(lectureEntity.getLectureEndDate());
        lectureDto.setStudentsApplyDeadline(lectureEntity.getStudentsApplyDeadline());
        lectureDto.setCtnt(lectureEntity.getCtnt());
        lectureDto.setTextbook(lectureEntity.getTextbook());
        lectureDto.setDelYn(lectureEntity.getDelYn());
        return lectureDto;
    }


}
