package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "student_semester_score")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class StudentSemesterScoreEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long grade;


    @Column(nullable = false, length = 10)
    private int score;


    @Column(name = "avg_score", updatable = true, nullable = true, length = 10)
    private int avgScore;


    @Column(length = 10)
    private double rating;


    @ManyToOne
    @JoinColumn(name = "isemester")
    @ToString.Exclude
    private SemesterEntity semesterEntity;

    @ManyToOne
    @JoinColumn(name = "student_num")
    @ToString.Exclude
    private StudentEntity studentEntity;

}
