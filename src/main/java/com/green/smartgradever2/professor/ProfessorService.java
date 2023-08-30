package com.green.smartgradever2.professor;


import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.entity.*;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.professor.model.*;
import com.green.smartgradever2.utils.FileUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final ProfessorMapper MAPPER;

    private final ProfesserRepository professorRepository;
    private final LectureApplyRepository lectureApplyRepository;


    @Value("${file.dir}")
    private String fileDir;


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


    @Transactional
    public ProfessorUpRes upProfessor(MultipartFile pic, ProfessorParam param) {
        ProfessorEntity professor = professorRepository.findByIprofessor(param.getIprofessor());

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
            throw new RuntimeException("스티커 사진 불가");
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
                throw new RuntimeException("스티커 사진 삭제 실패");
            }
        }
    }



    public ProfessorLctureSelRes selProfessorLecture(ProfessorSelLectureDto dto) {
        int itemsPerPage = 10; // 한 페이지당 수

        Pageable pageable = PageRequest.of(dto.getPage() - 1, itemsPerPage);
        Page<LectureApplyEntity> lecturePage = lectureApplyRepository.findAllByProfessorEntityIprofessor(dto.getIprofessor(), pageable);

        List<ProfessorLectureDto> lectures = new ArrayList<>();
        for (LectureApplyEntity lectureEntity : lecturePage.getContent()) {
            ProfessorLectureDto lectureDto = new ProfessorLectureDto();
            lectureDto.setIlecture(lectureEntity.getIlecture());
            lectureDto.setIlectureName(lectureEntity.getLectureNameEntity().getIlectureName());
            lectureDto.setIlectureRoom(lectureEntity.getLectureRoomEntity().getIlectureRoom());
            lectureDto.setIsemester(lectureEntity.getSemesterEntity().getIsemester());
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
            lectures.add(lectureDto);
        }

        int maxPage = lecturePage.getTotalPages();
        PagingUtils utils = new PagingUtils(dto.getPage(), maxPage);

        return ProfessorLctureSelRes.builder()
                .page(utils)
                .lectures(lectures)
                .build();
    }

}
