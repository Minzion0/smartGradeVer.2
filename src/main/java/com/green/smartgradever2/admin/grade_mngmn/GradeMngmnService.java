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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeMngmnService {

//    private final GradeMngmnRepository GM_REP;
//    private final AdminMajorRepository M_REP;
//    private final AdminLectureRoomRepository ALR_REP;
//    private final LectureStudentRepository LS_REP;
//    private final SemesterRepository SM_REP;
//    private final StudentRepository ST_REP;
//    private final GradeMngmnMapper MAPPER;


    private final GradeMngmnRepository GM_REP;
    private final AdminMajorRepository M_REP;
    private final AdminLectureRoomRepository ALR_REP;
    private final LectureStudentRepository LS_REP;
    private final SemesterRepository SM_REP;
    private final StudentRepository ST_REP;
    private final GradeMngmnMapper MAPPER;
    private final LectureApplyRepository APPLY_REP;
    private final LectureNameRepository NAME_REP;

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

    // todo 수정이 필요함
    public GradeMngmnUpdRes updGradeMngmn(GradeMngmnUpdParam p) {
        GradeMngmnUpdDto dto = new GradeMngmnUpdDto();
        dto.setIsemester(p.getIsemester());
        dto.setStudentNum(p.getStudentNum());

        GradeAvgVo vo = MAPPER.selAvgScore(dto);
        if (vo == null) {
            throw new RuntimeException("불러올 데이터가 존재하지 않습니다.");
        } else {
            dto.setAvgScore(vo.getAvgScore());
        }

        GradeUtils utils = new GradeUtils();
        int score = vo.getAvgScore();
        double v = utils.totalScore2(score);
        p.setAvgScore(score);

        dto.setAvgRating(v);
        int result = MAPPER.updAvgScore(dto);

        return GradeMngmnUpdRes.builder()
                .studentNum(p.getStudentNum())
                .isemester(p.getIsemester())
                .avgScore(p.getAvgScore())
                .avgRating(p.getAvgRating())
                .build();
    }

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
        List<LectureStudentEntity> list = LS_REP.findByLectureApplyEntity(applyEntity);

        log.info("list.size() : {}", list.size());

        int total;
        double avg;
        List<StudentEntity> student = ST_REP.findAll();

        List<LectureNameEntity> nameEntities = NAME_REP.findByLectureName(applyEntity.getLectureNameEntity().getLectureName());

        for (int i = 0; i < list.size(); i++) {
            total = list.get(i).getTotalScore();
            GradeUtils utils = new GradeUtils();
            avg = utils.totalScore2(total);
            log.info("avg : {}", avg);

            for (int z = 0; z < student.size(); z++) {

                StudentSemesterScoreEntity entity = StudentSemesterScoreEntity.builder()
                        .grade(student.get(z).getGrade()) // 학년
                        .rating(avg) // 평점
                        .score(nameEntities.get(z).getScore()) // 학점
                        .semesterEntity(semesterEntity) // isemester
                        .studentEntity(student.get(z)) // student pk
                        .build();

                GM_REP.save(entity);
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
}
