package com.green.smartgradever2.student;

import com.green.smartgradever2.entity.LectureApplyEntity;
import com.green.smartgradever2.entity.LectureScheduleEntity;
import com.green.smartgradever2.entity.LectureStudentEntity;
import com.green.smartgradever2.entity.StudentEntity;
import com.green.smartgradever2.lecture_apply.LectureApplyRepository;
import com.green.smartgradever2.lectureschedule.LectureScheduleRepository;
import com.green.smartgradever2.lecturestudent.LectureStudentRepository;
import com.green.smartgradever2.student.model.StudentParam;
import com.green.smartgradever2.student.model.StudentRegisterRes;
import com.green.smartgradever2.student.model.StudentUpRes;
import com.green.smartgradever2.utils.FileUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
        if (lectureApply.getLectureMaxPeople() <= lectureStudentRep.countByLectureAppllyEntity(lectureApply)) {
            response.setSuccess(false);
            response.setMessage("강의 등록이 가득찼습니다.");
            return response;
        }

        // 학생과 강의 정보를 수강 신청 테이블에 등록
        LectureStudentEntity lectureStudent = LectureStudentEntity.builder()
                .studentEntity(studentRep.findById(studentNum)
                        .orElseThrow(() -> new EntityNotFoundException("학생을 찾을수 없습니다.")))
                .lectureAppllyEntity(lectureApply)
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







}
