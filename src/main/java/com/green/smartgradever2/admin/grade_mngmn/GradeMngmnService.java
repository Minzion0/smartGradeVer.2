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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    private final GradeMngmnQdsl gradeMngmnQdsl;

    // 성적 수정 메소드
    public GradeMngmnUpdRes updGradeMngmn2(GradeMngmnUpdParam p) {

        SemesterEntity semesterEntity = SM_REP.findById(p.getIsemester()).get();
        log.info("semesterEntity.getIlectureStudent() : {}", semesterEntity.getIsemester());

        List<StudentEntity> studentEntities = ST_REP.findAll();
        GradeUtils utils = new GradeUtils();

        for (int i = 0; i < studentEntities.size(); i++) {
            List<LectureStudentEntity> studentEntityList = gradeMngmnQdsl.findAllBySemester(studentEntities.get(i), 1, semesterEntity);
            StudentSemesterScoreEntity entity = null;
            int temp = 0;
            int index = 0;
            int score = 0;
            double avg = 0;

            for (int z = 0; z < studentEntityList.size(); z++) {
                temp += studentEntityList.get(z).getTotalScore();
                score += studentEntityList.get(z).getLectureApplyEntity().getLectureNameEntity().getScore();
                avg += utils.totalScore2(temp);
                index++;
            }
            if (studentEntityList.size() != 0) {
                log.info("avg : {}", avg);
                entity = StudentSemesterScoreEntity.builder()
                    .grade(studentEntities.get(i).getGrade()) // 학년
                    .rating(utils.setDouRating(avg, index)) // 평점
                    .score(score) // 학점
                    .avgScore(temp / index) // 평균 총점
                    .semesterEntity(semesterEntity) // semester pk
                    .studentEntity(studentEntities.get(i)) // studentEntities pk
                    .build();
                long stop1 = System.currentTimeMillis();
                log.info("stop1 : {}", stop1);
                try {
                    GM_REP.save(entity);
                } catch (Exception e) {
                    throw new RuntimeException("중복된 값이 존재합니다.");
                }
            }
        }
        return null;
    }

    // 성적 조회 메소드
    public GradeMngmnFindRes selGradeMngmn(GradeMngmnDto dto, Pageable pageable) {
        long maxPage = gradeMngmnQdsl.countByStudentEntity(dto);
        PagingUtils utils = new PagingUtils(dto.getPage(), (int) maxPage);
        dto.setStaIdx(utils.getStaIdx());

        List<GradeMngmnVo> voList = gradeMngmnQdsl.studentVo(dto, pageable);
        String rating;
        int point;
        double score;
        for (GradeMngmnVo a : voList) {
            point = a.getTotalScore();
            GradeUtils utils2 = new GradeUtils(point);
            score = utils2.totalScore();
            rating = utils2.totalStrRating(score);
            a.setRating(rating);
        }
        GradeMngmnStudentVo studentVo = gradeMngmnQdsl.mngmnStudentVo(dto);
        List<GradeMngmnAvgVo> avg = gradeMngmnQdsl.avgVo(dto);

        return GradeMngmnFindRes.builder()
            .voList(voList)
            .student(studentVo)
            .avgVo(avg)
            .page(utils)
            .build();
    }

    // 학생 성적 세부 조회 메소드
    public Optional<GradeMngmnDetailVo> selStudentDetail(GradeMngmnDetailSelDto dto) throws NullPointerException {
        Optional<GradeMngmnDetailVo> gradeMngmnDetailVo = gradeMngmnQdsl.studentDetail(dto);
        return gradeMngmnDetailVo;
    }

    // 학생 성적 세부 조회 메소드
    public GradeMngmnDetailVo selStudentDetail2(GradeMngmnDetailSelDto dto) {
        Optional<GradeMngmnDetailVo> gradeMngmnDetailVo = GM_REP.selStudentDetail(dto.getStudentNum());
        return gradeMngmnDetailVo.orElse(new GradeMngmnDetailVo()); // 기본값으로 객체 생성
    }
}