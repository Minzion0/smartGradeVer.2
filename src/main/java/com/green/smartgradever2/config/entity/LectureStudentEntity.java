package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "lecture_student")
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class LectureStudentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ilecture_student",updatable = false,nullable = false,columnDefinition = "BIGINT UNSIGNED")
    private Long ilectureStudent;

    @ManyToOne
    @JoinColumn(name = "studentNum")
    @ToString.Exclude
    private StudentEntity studentEntity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ilecture")
    @ToString.Exclude
    private LectureApplyEntity lectureApplyEntity;

    @Column(name = "finished_yn",nullable = false)
    @ColumnDefault("0")
    private int finishedYn;

    @Column
    @ColumnDefault("0")
    private int attendance;

    @Column(name = "midterm_examination")
    @ColumnDefault("0")
    private int midtermExamination;

    @Column(name = "final_Examination")
    @ColumnDefault("0")
    private int finalExamination;

    @Column(name = "total_score")
    @ColumnDefault("0")
    private int totalScore;

    @Column(name = "finished_at")
    private LocalDate finishedAt;

    @Column(name = "correction_at")
    private LocalDate correctionAt;

    @Column(name = "objection")
    @ColumnDefault("0")
    private int objection;

}
