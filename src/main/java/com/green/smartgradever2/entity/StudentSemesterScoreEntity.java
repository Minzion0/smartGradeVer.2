package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(
        name="student_semester_score",

        uniqueConstraints={
                @UniqueConstraint(
                        name = "uniqu",
                        columnNames = {
                                "isemester",
                                "student_num"
                        }
                ),
        })
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class StudentSemesterScoreEntity extends BaseEntity {

    /**
     * pk값
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "istmester_score", updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long ismesterScore;

    /**
     * 학점
     **/
    @Column(nullable = false, length = 10)
    private int score;

    /**
     * 평균점수
     **/
    @Column(name = "avg_score", updatable = true, nullable = true, length = 10)
    private int avgScore;

    /**
     * 평점
     **/
    @Column(length = 10)
    private double rating;

    /**
     * 학기pk
     **/
    @ManyToOne
    @JoinColumn(name = "isemester")
    @ToString.Exclude

    private SemesterEntity semesterEntity;

    /**
     * 학생 pk
     **/
    @ManyToOne
    @JoinColumn(name = "student_num")
    @ToString.Exclude
    private StudentEntity studentEntity;

    @Column(length = 10, nullable = false)
    private Long grade;
}
