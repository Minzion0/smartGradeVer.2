package com.green.smartgradever2.admin.major;

import com.green.smartgradever2.entity.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMajorRepository extends JpaRepository<MajorEntity, Long> {
    List<MajorEntity> findAllByDelYnAndMajorName(int delYn, String majorName);
//todo 이게 왜 필요한건가?? 알려주세용
//    MajorEntity findByStudentNum(Integer studentNum);

}
