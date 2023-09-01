package com.green.smartgradever2.student;

import com.green.smartgradever2.admin.professor.model.AdminProfessorLectureVo;
import com.green.smartgradever2.config.entity.*;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lectureschedule.LectureScheduleRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.student.model.*;
import com.green.smartgradever2.utils.FileUtils;
import com.green.smartgradever2.utils.GradeUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final StudentMapper MAPPER;
    private final StudentRepository studentRep;
    private final LectureApplyRepository lectureApplyRep;
    private final LectureStudentRepository lectureStudentRep;
    private final LectureScheduleRepository lectureScheduleRep;
    private final PasswordEncoder PW_ENCODER;



    @Value("${file.dir}")
    private String fileDir;

    @Transactional
    public StudentUpRes upStudent(MultipartFile pic, StudentParam param) {
        StudentEntity student = studentRep.findBystudentNum(param.getStudentNum());

        if (student == null) {
            return null;
        }
        student.setAddress(param.getAddress());
        student.setEmail(param.getEmail());
        student.setPhone(param.getPhone());

        if (pic != null) {
            String centerPath = String.format("students/%d", student.getStudentNum());
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

            student.setPic(savedFileName);
        }
        try {
            studentRep.save(student);
        } catch (Exception e) {
            // 업로드에 실패한 경우 파일 삭제
            if (pic != null) {
                String picFileName = student.getPic();
                String picPath = String.format("students/%d/%s", student.getStudentNum(), picFileName);
                String fullPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), picPath);

                File picFile = new File(fullPath);
                if (picFile.exists()) {
                    picFile.delete();
                }
            }
            throw new RuntimeException("사진 불가");
        }


        return new StudentUpRes(student);
    }


    @Transactional
    public void studentDelPic(Long studentNum) {
        StudentEntity studnet = studentRep.findBystudentNum(studentNum);

        if (studnet != null && studnet.getPic() != null) {
            //기존 사진이 있으면 삭제
            String picFileName = studnet.getPic();
            String picPath = String.format("students/%d/%s", studentNum, picFileName);
            String fullPath = String.format("%s/%s", FileUtils.getAbsolutePath(fileDir), picPath);

            File picFile = new File(fullPath);
            if (picFile.exists()) {
                picFile.delete();
            }

            studnet.setPic(null);

            try {
                studentRep.save(studnet);
            } catch (Exception e) {
                throw new RuntimeException("사진 삭제 실패");
            }


        }


    }



        //수강신청
    public StudentRegisterRes registerLectureForStudent(Long ilecture, Long studentNum) {
        StudentRegisterRes response = new StudentRegisterRes();
        StudentEntity student = studentRep.findBystudentNum(studentNum);
        // 학생이 수강하려는 강의 정보 가져오기
        LectureApplyEntity lectureApply = lectureApplyRep.findById(ilecture)
                .orElseThrow(() -> new EntityNotFoundException("강의를 찾을수 없습니다."));

        // 해당 강의의 수강 인원 확인
        if (lectureApply.getLectureMaxPeople() <= lectureStudentRep.countByLectureApplyEntity(lectureApply)) {
            response.setSuccess(false);
            response.setMessage("강의 등록이 가득찼습니다.");
            return response;
        }

        if (lectureStudentRep.existsByStudentEntityAndLectureApplyEntity(student, lectureApply)) {
            response.setSuccess(false);
            response.setMessage("이미 신청한 강의입니다.");
            return response;
        }


        // 학생과 강의 정보를 수강 신청 테이블에 등록
        LectureStudentEntity lectureStudent = LectureStudentEntity.builder()
                .studentEntity(studentRep.findById(studentNum)
                        .orElseThrow(() -> new EntityNotFoundException("학생을 찾을수 없습니다.")))
                .lectureApplyEntity(lectureApply)
                .build();
        lectureStudentRep.save(lectureStudent);





        //수강 신청한 강의 시작시간 ,종료시간 , 시작요일
        LectureScheduleEntity lectureSchedule = lectureScheduleRep.findByIlecture(ilecture);
        if (lectureSchedule != null) {
            response.setDayWeek(lectureSchedule.getDayWeek());
            response.setLectureStrTime(lectureSchedule.getLectureStrTime());
            response.setLectureEndTime(lectureSchedule.getLectureEndTime());
        }




        response.setSuccess(true);
        response.setMessage("수상 신청 성공.");
        response.setLectureName(lectureApply.getLectureNameEntity().getLectureName()); // 강의 이름 설정
        response.setIlectureStudent(lectureStudent.getIlectureStudent());
        response.setIlecture(lectureApply.getIlecture());
        response.setStudentNum(Long.valueOf(lectureStudent.getStudentEntity().getStudentNum()));
        response.setAttendance(lectureStudent.getAttendance());
        response.setMidtermExamination(lectureStudent.getMidtermExamination());
        response.setFinalExamination(lectureStudent.getFinalExamination());
        response.setTotalScore(lectureStudent.getTotalScore());
        response.setFinishedAt(lectureStudent.getFinishedAt());
        response.setCorrectionAt(lectureStudent.getCorrectionAt());
        response.setFinishedYn(lectureStudent.getFinishedYn());
        response.setDelYn(lectureStudent.getDelYn());
        response.setObjection(lectureStudent.getObjection());
        // 필요한 정보를 저장하는 로직 추가

        return response;
    }

    //학생프로필 디데일
    public StudentFileSelRes getStudentProfileWithLectures(Long studentNum) {
        StudentEntity student = studentRep.findById(studentNum).get();
        if (student == null) {
            log.error("학생이 없습니다.: {}", studentNum);
            return null;
        }
        StudentProfileVo profile = new StudentProfileVo();
        profile.setStudentNum(student.getStudentNum());
        profile.setName(student.getNm());
        profile.setMajorName(student.getMajorEntity().getMajorName());
        profile.setGrade(student.getGrade());
        profile.setGender(student.getGender());
        profile.setBirthDate(student.getBirthDate());
        profile.setAddress(student.getAddress());
        profile.setPhone(student.getPhone());
        profile.setEmail(student.getEmail());
        profile.setGender(student.getGender());
        profile.setPic(student.getPic());
        profile.setFinishedYn(student.getFinishedYn());

        List<LectureStudentEntity> lectureApplyEntityList = lectureStudentRep.findByStudentEntity(student);
        List<StudentProfileLectureVo> lectureList = lectureApplyEntityList.stream().filter(lecture ->lecture.getFinishedYn() ==0 ).map(lecture -> {
            LectureApplyEntity lectureApplyEntity = lecture.getLectureApplyEntity();
            LectureScheduleEntity lectureScheduleEntity = lectureApplyEntity.getLectureScheduleEntity();

            LectureNameEntity lectureNameEntity = lectureApplyEntity.getLectureNameEntity();
            int lectureScore = lectureNameEntity.getScore(); // 강의 학점

            StudentProfileLectureVo lectureVo = StudentProfileLectureVo.builder()
                    .ilecture(lectureApplyEntity.getIlecture())
                    .lectureStrTime(lectureScheduleEntity.getLectureStrTime())
                    .lectureEndTime(lectureScheduleEntity.getLectureEndTime())
                    .lectureStrDate(lectureApplyEntity.getSemesterEntity().getSemesterStrDate())
                    .lectureEndDate(lectureApplyEntity.getSemesterEntity().getSemesterEndDate())
                    .lectureName(lectureNameEntity.getLectureName())
                    .score(lectureScore) // 강의 학점 설정
                    .build();

            return lectureVo;
        }).toList();

        int totalScore = lectureApplyEntityList.stream()
                .filter(lecture -> lecture.getFinishedYn() == 1) //
                .mapToInt(lecture -> lecture.getLectureApplyEntity().getLectureNameEntity().getScore())
                .sum();

        // 총 학점을 프로필에 설정
        profile.setScore(totalScore);


        StudentFileSelRes result = StudentFileSelRes.builder()
                .profile(profile)
                .lectureList(lectureList)
                .build();

        return result;


    }


    // 학생 강의별 성적
    public List<StudentSelVo> getStudentLectureGrades(StudentEntity studentEntity) {
        List<LectureStudentEntity> lectureGrades = lectureStudentRep.findByStudentEntity(studentEntity);
        List<StudentSelVo> studentSelVos = new ArrayList<>(); // 각 강의별 성적 정보를 담을 리스트

        for (LectureStudentEntity lectureGrade : lectureGrades) {
                StudentSelVo vo = new StudentSelVo();

                // 강의별 성적 정보를 각각의 필드에 설정
            vo.setStudentNum(studentEntity.getStudentNum());
            vo.setIlectureStudent(lectureGrade.getIlectureStudent());
            vo.setIlecture(lectureGrade.getLectureApplyEntity().getIlecture());
            vo.setStudentGrade(lectureGrade.getStudentEntity().getGrade());
            vo.setIsemester(lectureGrade.getLectureApplyEntity().getSemesterEntity().getIsemester());
            vo.setYear(lectureGrade.getLectureApplyEntity().getSemesterEntity().getYear());
            vo.setProfessorName(lectureGrade.getLectureApplyEntity().getProfessorEntity().getNm());
            vo.setLectureName(lectureGrade.getLectureApplyEntity().getLectureNameEntity().getLectureName());
            vo.setDayWeek(lectureGrade.getLectureApplyEntity().getLectureScheduleEntity().getDayWeek());
            vo.setLectureStrTime(lectureGrade.getLectureApplyEntity().getLectureScheduleEntity().getLectureStrTime());
            vo.setLectureEndTime(lectureGrade.getLectureApplyEntity().getLectureScheduleEntity().getLectureEndTime());
            vo.setScore(lectureGrade.getLectureApplyEntity().getLectureNameEntity().getScore());

            vo.setFinishedYn(lectureGrade.getFinishedYn());
            vo.setAttendance(lectureGrade.getAttendance());
            vo.setMidtermExamination(lectureGrade.getMidtermExamination());
            vo.setFinalExamination(lectureGrade.getFinalExamination());
            vo.setTotalScore(lectureGrade.getTotalScore());


                GradeUtils gradeUtils = new GradeUtils();
                String grade = gradeUtils.totalGradeFromScore(lectureGrade.getTotalScore());
                vo.setGrade(grade); // 점수를 소수점 형태의 평점 문자열로 설정

                // 학점 계산 및 설정
                double score = gradeUtils.totalScore();
                String rating = gradeUtils.totalRating(score);
                vo.setRating(rating);

                studentSelVos.add(vo); // 각각의 강의별 성적 정보를 리스트에 추가
            }

        return studentSelVos;  // 강의별 성적 정보 리스트 반환
    }

    public StudentEntity getStudentById(Long studentNum) {

        return studentRep.findBystudentNum(studentNum);
    }


    // 학생 학점 조회
    public StudentInfoDto getStudentInfo(Long studentNum) {
        StudentEntity student = studentRep.findBystudentNum(studentNum);
        int selfStudyCredit = calculateSelfStudyCredit(studentNum);
        String majorName = student.getMajorEntity().getMajorName();
        int graduationScore = student.getMajorEntity().getGraduationScore();
        int remainingPoints = graduationScore - selfStudyCredit;

        StudentInfoDto studentInfoDTO = new StudentInfoDto();
        studentInfoDTO.setStudentNum(studentNum);
        studentInfoDTO.setSelfStudyCredit(selfStudyCredit);
        studentInfoDTO.setGraduationScore(graduationScore);
        studentInfoDTO.setRemainingPoints(remainingPoints);
        studentInfoDTO.setMajorName(majorName);

        return studentInfoDTO;
    }

    public int calculateSelfStudyCredit(Long studentNum) {
        List<LectureStudentEntity> finishedLectures = lectureStudentRep.findByStudentEntityStudentNumAndFinishedYn(studentNum, 1);
        int totalCredit = 0;

        for (LectureStudentEntity lectureStudent : finishedLectures) {
            totalCredit += lectureStudent.getLectureApplyEntity().getLectureNameEntity().getScore();
        }

        return totalCredit;
    }

    /** 학생 비밀번호 변경 (로그인 완료 시 가능) **/
    public String updPassword(StudentPasswordParam param, StudentUpdPasswordDto dto) throws Exception{
        StudentEntity entity = studentRep.findById(dto.getIstudent()).get();

        if (!PW_ENCODER.matches(param.getCurrentPassword(), entity.getStudentPassword())) {
            throw new Exception("기존 비밀번호를 다시 확인해주세요");
        }
        String npw = PW_ENCODER.encode(param.getStudentPassword());
        entity.setStudentPassword(npw);
        studentRep.save(entity);

        return "비밀번호 변경이 완료되었습니다.";
    }




}
