package com.green.smartgradever2.student;

import com.green.smartgradever2.config.entity.LectureApplyEntity;
import com.green.smartgradever2.config.entity.LectureScheduleEntity;
import com.green.smartgradever2.config.entity.LectureStudentEntity;
import com.green.smartgradever2.config.entity.StudentEntity;
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
    public void studentDelPic(int studentNum) {
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

        // 학생이 수강하려는 강의 정보 가져오기
        LectureApplyEntity lectureApply = lectureApplyRep.findById(ilecture)
                .orElseThrow(() -> new EntityNotFoundException("강의를 찾을수 없습니다."));

        // 해당 강의의 수강 인원 확인
        if (lectureApply.getLectureMaxPeople() <= lectureStudentRep.countByLectureApplyEntity(lectureApply)) {
            response.setSuccess(false);
            response.setMessage("강의 등록이 가득찼습니다.");
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
        // 필요한 정보를 저장하는 로직 추가

        return response;
    }

    //학생프로필 디데일
    public StudentProfileDto getStudentProfileWithLectures(int studentNum) {
        StudentEntity student = studentRep.findBystudentNum(studentNum);
        if (student == null) {
            log.error("Student not found with studentNum: {}", studentNum);
            return null;
        }

        // 학생이 수강 중인 강의 정보
        List<LectureStudentEntity> attendedLectureEntities = lectureStudentRep.findByStudentEntity(student);

        StudentProfileDto studentProfileDto = new StudentProfileDto();
        studentProfileDto.setStudentNum(Long.valueOf(student.getStudentNum()));
        studentProfileDto.setImajor(student.getMajorEntity().getImajor());
        studentProfileDto.setIsemester(student.getSemesterEntity().getIsemester());
        studentProfileDto.setNm(student.getNm());
        studentProfileDto.setGrade(student.getGrade());
        studentProfileDto.setGender(student.getGender());
        studentProfileDto.setAddress(student.getAddress());
        studentProfileDto.setPhone(student.getPhone());
        studentProfileDto.setBirthDate(student.getBirthdate());
        studentProfileDto.setEmail(student.getEmail());
        studentProfileDto.setPic(student.getPic());
        studentProfileDto.setFinishedYn(student.getFinishedYn());
        studentProfileDto.setRole(student.getRole());

        // 수강 중인 강의 정보를 가져와서 StudentLectureDto 리스트로 변환
        List<StudentLectureDto> studentLectures = new ArrayList<>();
        for (LectureStudentEntity lectureStudentEntity : attendedLectureEntities) {
            StudentLectureDto studentLectureDto = new StudentLectureDto();
            studentLectureDto.setIlectureStudent(lectureStudentEntity.getIlectureStudent());
            studentLectureDto.setIlecture(lectureStudentEntity.getLectureApplyEntity().getIlecture());
            studentLectureDto.setFinishedYn(lectureStudentEntity.getFinishedYn());
            studentLectureDto.setAttendance(lectureStudentEntity.getAttendance());
            studentLectureDto.setMidtermExamination(lectureStudentEntity.getMidtermExamination());
            studentLectureDto.setFinalExamination(lectureStudentEntity.getFinalExamination());
            studentLectureDto.setTotalScore(lectureStudentEntity.getTotalScore());
            studentLectureDto.setFinishedAt(lectureStudentEntity.getFinishedAt());
            studentLectureDto.setCorrectionAt(lectureStudentEntity.getCorrectionAt());
            studentLectureDto.setDayWeek(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getDayWeek());
            studentLectureDto.setLectureStrTime(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getLectureStrTime());
            studentLectureDto.setLectureEndTime(lectureStudentEntity.getLectureApplyEntity().getLectureScheduleEntity().getLectureEndTime());
            studentLectures.add(studentLectureDto);
        }

        studentProfileDto.setAttendedLectures(studentLectures);

        return studentProfileDto;
    }



    // 학생 강의별 성적
    public List<StudentSelVo> getStudentLectureGrades(StudentEntity studentEntity) {
        List<LectureStudentEntity> lectureGrades = lectureStudentRep.findByStudentEntity(studentEntity);
        List<StudentSelVo> studentSelVos = new ArrayList<>(); // 각 강의별 성적 정보를 담을 리스트

        for (LectureStudentEntity lectureGrade : lectureGrades) {
            StudentSelVo vo = new StudentSelVo(); // 새로운 StudentSelVo 객체 생성

            // 강의별 성적 정보를 각각의 필드에 설정
            vo.setStudentNum(studentEntity.getStudentNum());
            vo.setIlectureStudent(lectureGrade.getIlectureStudent());
            vo.setIlecture(lectureGrade.getLectureApplyEntity().getIlecture());
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

    public StudentEntity getStudentById(Integer studentNum) {

        return studentRep.findByStudentNum(studentNum);
    }





}
