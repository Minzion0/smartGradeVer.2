package com.green.smartgradever2.admin.professor;

import com.green.smartgradever2.admin.professor.model.*;
import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.MajorEntity;
import com.green.smartgradever2.config.entity.ProfessorEntity;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProfessorService {

    private final AdminProfessorRepository RPS;
    private final LectureApplyRepository LECTURE_RPS;
    private final AdminProfessorMapper MAPPER;
    private final PasswordEncoder PW_ENCODER;
    private final EntityManager EM;

    @Transactional
    public AdminProfessorInsVo insProfessor(AdminProfessorInsParam param){

        MajorEntity major = new MajorEntity();
        major.setImajor(param.getImajor());

        Optional<List<ProfessorEntity>> orderByIprofessor = RPS.findAllByOrderByIprofessor();
        int size=1;
        if (!orderByIprofessor.isEmpty()){
            size = orderByIprofessor.get().size()+1;
        }
        long iprofessor = 100000 + size;


        String setPw = param.getBirthdate().toString().replaceAll("-", "");

        ProfessorEntity professor = new ProfessorEntity();
            professor.setIprofessor(iprofessor);
            professor.setProfessorPassword(PW_ENCODER.encode(setPw));
            professor.setPhone(param.getPhone());
            professor.setNm(param.getNm());
            professor.setGender(param.getGender());
            professor.setBirthDate(param.getBirthdate());
            professor.setMajorEntity(major);

        RPS.saveAndFlush(professor);

        EM.detach(professor);

        ProfessorEntity entity = RPS.findById(professor.getIprofessor()).get();

       return AdminProfessorInsVo.builder().iprofessor(entity.getIprofessor())
                .imajor(entity.getMajorEntity().getImajor())
                .nm(entity.getNm())
                .gender(entity.getGender())
                .birthdate(entity.getBirthDate())
                .phone(entity.getPhone())
                .createdAt(entity.getCreatedAt())
                .delYn(entity.getDelYn())
                .build();
    }


    public AdminProfessorFindRes findProfessors(Pageable pageable, AdminProfessorFindParam param){
        Page<ProfessorEntity> list=null;

        if (param.getName()!=null&&param.getImajor()!=0){
            MajorEntity major = new MajorEntity();
            major.setImajor(param.getImajor());
          list=  RPS.findAllByNmAndMajorEntity(param.getName(),major,pageable);
        }else if (param.getName()!=null){
          list=  RPS.findAllByNm(param.getName(),pageable);
        } else if (param.getImajor() != 0) {
            MajorEntity major = new MajorEntity();
            major.setImajor(param.getImajor());
           list= RPS.findAllByMajorEntity(major,pageable);
        }else {
             list = RPS.findAll(pageable);
        }

        PagingUtils pagingUtils = new PagingUtils(pageable.getPageNumber(),(int)list.getTotalElements());


        List<AdminProfessorFindVo> professors = list.getContent().stream().map(item -> AdminProfessorFindVo.builder()
                .iprofessor(item.getIprofessor())
                .majorName(item.getMajorEntity().getMajorName())
                .nm(item.getNm())
                .gender(item.getGender())
                .birthdate(item.getBirthDate())
                .phone(item.getPhone())
                .email(item.getEmail())
                .address(item.getAddress())
                .createdAt(item.getCreatedAt())
                .delYn(item.getDelYn()).build()).toList();

        return AdminProfessorFindRes.builder().professors(professors).page(pagingUtils).build();

    }

    public AdminProfessorDetailRes findProfessorDetail(Long iprofessor){
        ProfessorEntity entity = RPS.findById(iprofessor).get();
        AdminProfessorProfileVo profile = new AdminProfessorProfileVo();
            profile.setIprofessor(entity.getIprofessor());
            profile.setName(entity.getNm());
            profile.setGender(entity.getGender());
            profile.setBirthdate(entity.getBirthDate());
            profile.setPhone(entity.getPhone());
            profile.setPic(entity.getPic());
            profile.setAddress(entity.getAddress());
            profile.setEmail(entity.getEmail());
            profile.setImajor(entity.getMajorEntity().getImajor());
            profile.setCreatedAt(entity.getCreatedAt());
            profile.setDelYn(entity.getDelYn());

        List<LectureApplyEntity> lectureEntityList = LECTURE_RPS.findByProfessorEntity(entity);

        List<AdminProfessorLectureVo> lectureList = lectureEntityList.stream().map(lecture -> AdminProfessorLectureVo.builder()
            .ilecture(lecture.getIlecture())
            .lectureStrTime(lecture.getLectureScheduleEntity().getLectureStrTime())
            .lectureEndTime(lecture.getLectureScheduleEntity().getLectureEndTime())
            .lectureStrDate(lecture.getSemesterEntity().getSemesterStrDate())
            .lectureEndDate(lecture.getSemesterEntity().getSemesterEndDate())
            .lectureName(lecture.getLectureNameEntity().getLectureName()).build()).toList();

       return AdminProfessorDetailRes.builder().lectureList(lectureList).profile(profile).build();

    }




}
