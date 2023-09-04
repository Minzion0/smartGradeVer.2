package com.green.smartgradever2.admin.grade_mngmn;

import com.green.smartgradever2.admin.grade_mngmn.model.*;
import com.green.smartgradever2.admin.lecturename.LectureNameRepository;
import com.green.smartgradever2.admin.lectureroom.AdminLectureRoomRepository;
import com.green.smartgradever2.admin.major.AdminMajorRepository;
import com.green.smartgradever2.admin.semester.SemesterRepository;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.student.StudentRepository;
import com.green.smartgradever2.utils.GradeUtils;
import com.green.smartgradever2.utils.PagingUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeMngmnService {


    private final GradeMngmnRepository GM_REP;
    private final AdminMajorRepository M_REP;
    private final AdminLectureRoomRepository ALR_REP;
    private final LectureStudentRepository LS_REP;
    private final SemesterRepository SM_REP;
    private final StudentRepository ST_REP;
    private final GradeMngmnMapper MAPPER;
    private final LectureApplyRepository APPLY_REP;
    private final LectureNameRepository NAME_REP;
    private final EntityManager EM;


    public GradeMngmnRes postGradeMngmn(GradeMngmnInsDto dto) {
        GradeMngmnDto mngmnDto = new GradeMngmnDto();
        int result = MAPPER.insGradeMngmn(dto);
        List<GradeMngmnVo> voList = MAPPER.selGradeFindStudent(mngmnDto);
        int point;
        double score;
        for (GradeMngmnVo a : voList) {
            point = a.getTotalScore();
            GradeUtils utils2 = new GradeUtils(point);
            score = utils2.totalScore();

            if (result == 1) {
                GradeMngmnRes.builder()
                        .isemester(dto.getIsemester())
                        .studentNum(dto.getStudentNum())
                        .ilectureName(dto.getIlectureName())
                        .semester(dto.getSemester())
                        .avgScore(point)
                        .rating(score)
                        .build();
            }

        }
        return null;
    }

//    //  수정이 필요함
//    public GradeMngmnUpdRes updGradeMngmn(GradeMngmnUpdParam p) {
//        GradeMngmnUpdDto dto = new GradeMngmnUpdDto();
//        dto.setIsemester(p.getIsemester());
//        dto.setStudentNum(p.getStudentNum());
//
//        GradeAvgVo vo = MAPPER.selAvgScore(dto);
//        if (vo == null) {
//            throw new RuntimeException("불러올 데이터가 존재하지 않습니다.");
//        } else {
//            dto.setAvgScore(vo.getAvgScore());
//        }
//
//        GradeUtils utils = new GradeUtils();
//        int score = vo.getAvgScore();
//        double v = utils.totalScore2(score);
//        p.setAvgScore(score);
//
//        dto.setAvgRating(v);
//        int result = MAPPER.updAvgScore(dto);
//
//        return GradeMngmnUpdRes.builder()
//                .studentNum(p.getStudentNum())
//                .isemester(p.getIsemester())
//                .avgScore(p.getAvgScore())
//                .avgRating(p.getAvgRating())
//                .build();
//    }

    public GradeMngmnUpdRes updGradeMngmn2(GradeMngmnUpdParam p) {
//        StudentSemesterScoreEntity entity = new StudentSemesterScoreEntity();
//        SemesterEntity semesterEntity = SM_REP.findById(p.getIsemester()).get();
//        StudentEntity studentEntity = ST_REP.findById(p.getStudentNum()).get();
//        GradeUtils utils = new GradeUtils();
//
//        entity.setSemesterEntity(semesterEntity);
//        entity.setStudentEntity(studentEntity);
//        List<StudentSemesterScoreEntity> allBy = GM_REP.findAllByStudentEntityAndSemesterEntity(entity.getStudentEntity(),entity.getSemesterEntity());
//        if (allBy == null) {
//            throw new RuntimeException("불러올 데이터가 존재하지 않습니다.");
//        } else {
//            for (StudentSemesterScoreEntity scoreEntity : allBy) {
//                entity.setAvgScore(scoreEntity.getAvgScore());
//                double v = utils.totalScore2(entity.getAvgScore());
//                entity.setRating(v);
//            }
//        }
//        StudentSemesterScoreEntity save = GM_REP.save(entity);
//
//        return GradeMngmnUpdRes.builder()
//                .studentNum(studentEntity.getStudentNum())
//                .isemester(semesterEntity.getIsemester())
//                .avgScore(entity.getAvgScore())
//                .avgRating(entity.getRating())
//                .build();
        SemesterEntity semesterEntity = SM_REP.findById(p.getIsemester()).get();
        log.info("semesterEntity.getIlectureStudent() : {}", semesterEntity.getIsemester());

        LectureApplyEntity applyEntity = APPLY_REP.findById(semesterEntity.getIsemester()).get();
        log.info("applyEntity.get().getIlecture() : {}", applyEntity.getIlecture());

        LectureStudentEntity lectureStudentEntity = LS_REP.findById(applyEntity.getIlecture()).get();
        List<LectureStudentEntity> list = LS_REP.findAllByLectureApplyEntity(applyEntity);
        log.info("list.size() : {}", list.size());

        int total;
        double avg;

        List<StudentEntity> studentEntities = ST_REP.findAll();
        List<LectureNameEntity> nameEntities = NAME_REP.findByLectureName(applyEntity.getLectureNameEntity().getLectureName());
        GradeUtils utils = new GradeUtils();

            for (int i = 0; i < studentEntities.size(); i++) {
                List<LectureStudentEntity> studentEntityList = LS_REP.findAllByStudentEntityAndFinishedYn(studentEntities.get(i), 1);
                StudentSemesterScoreEntity entity = null;
                int temp = 0;
                int index = 0;
                int score = 0;

                for (int z = 0; z < studentEntityList.size(); z++) {
                    temp += studentEntityList.get(z).getTotalScore();
                    score += studentEntityList.get(z).getLectureApplyEntity().getLectureNameEntity().getScore();
                    index++;
                }
                if (studentEntityList.size() != 0) {
                    avg = utils.totalScore2(temp / index);
                    log.info("avg : {}", avg);
                    entity = StudentSemesterScoreEntity.builder()
                            .grade(studentEntities.get(i).getGrade()) // 학년
                            .rating(avg) // 평점
                            .score(score) // 학점
                            .avgScore(temp / index) // 평균 총점
                            .semesterEntity(semesterEntity) // semester pk
                            .studentEntity(studentEntities.get(i)) // studentEntities pk
                            .build();
                    try {
                        GM_REP.save(entity);
                    } catch (Exception e) {
                        throw new RuntimeException("중복된 값이 존재합니다.");
                    }
                }
            }
        return null;
    }



    public GradeMngmnFindRes selGradeMngmn(GradeMngmnDto dto) {
        int maxPage = MAPPER.countStudent();
        PagingUtils utils = new PagingUtils(dto.getPage(), maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<GradeMngmnAvgVo> avg = MAPPER.GradeMngmnAvg(dto);
        GradeMngmnStudentVo vo = MAPPER.selGradeMngmnStudent(dto);
        List<GradeMngmnVo> voList = MAPPER.selGradeFindStudent(dto);

        int point;
        double score;
        String rate;
        for (GradeMngmnVo a : voList) {
            point = a.getTotalScore();
            GradeUtils utils2 = new GradeUtils(point);
            score = utils2.totalScore();
            rate = utils2.totalRating(score);
            a.setRating(rate);
        }

        return GradeMngmnFindRes.builder()
                .voList(voList)
                .student(vo)
                .avgVo(avg)
                .paging(utils)
                .build();
    }

    public GradeMngmnDetailVo selStudentDetail(GradeMngmnDetailSelDto dto) {
        return MAPPER.selGradeFindStudentDetail(dto);
    }

    public GradeMngmnDetailVo selStudentDetail2(GradeMngmnDetailSelDto dto) {
        String jpql = "select s.pic, s.nm, s.gender, s.birthdate, s.phone, s.address, s.studentNum" +
                ", m.majorName, s.createdAt, s.email, sssc.score, m.graduationScore" +
                ", s.grade, sssc.semesterEntity, lc.lectureApplyEntity" +
                ", sssc.avgScore, avg(sssc.rating)" +
                " from LectureStudentEntity lc " +
                "LEFT join lc.studentEntity s " +
                "inner join s.majorEntity m " +
                "inner join  s.ssscList sssc where s.studentNum = :studentNum";
        EM.createQuery(jpql, LectureStudentEntity.class).setParameter("GradeMngmnDetailSelDto", dto).getResultList();
        StudentEntity stEntity = ST_REP.findById(dto.getStudentNum()).get();
        StudentSemesterScoreEntity sscEntity = GM_REP.findById(dto.getStudentNum()).get();
        LectureStudentEntity lsEntity = LS_REP.findById(sscEntity.getStudentEntity().getStudentNum()).get();
        MajorEntity mEntity = M_REP.findById(stEntity.getStudentNum()).get();

        return GradeMngmnDetailVo.builder()
                .studentNum(dto.getStudentNum())
                .address(stEntity.getAddress())
                .scoreStudent(sscEntity.getScore())
                .createdAt(stEntity.getCreatedAt())
                .phone(stEntity.getPhone())
                .pic(stEntity.getPic())
                .graduationScore(mEntity.getGraduationScore())
                .birthDate(stEntity.getBirthdate())
                .gender(stEntity.getGender())
                .email(stEntity.getEmail())
                .name(stEntity.getNm())
                .majorName(mEntity.getMajorName())
                .build();
    }


}
