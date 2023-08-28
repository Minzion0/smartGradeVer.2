package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Time;
import java.time.LocalTime;

@Entity
@Table(name = "lecture_schedule")
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@ToString(callSuper = true)
public class LectureScheduleEntity {

    //todo pk 수정이 필요함
    @Id
    @OneToOne
    @JoinColumn(name = "ilectuer")
    @ToString.Exclude
    private LectureApplyEntity lectureApplyEntity;



//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
//    private Long ilecture;

    @ManyToOne
    @JoinColumn(name = "isemester")
    @ToString.Exclude
    private SemesterEntity semesterEntity;

    @Column(name = "day_week", nullable = false ,length = 10)
    private int dayWeek;

    @Column(name = "lecture_str_time",nullable = false)
    private LocalTime lectureStrTime;

    @Column(name = "lecture_end_time", nullable = false)
    private LocalTime lectureEndTime;

    @Column(name = "del_yn", length = 10)
    @ColumnDefault("0")
    private int delYn;

}
