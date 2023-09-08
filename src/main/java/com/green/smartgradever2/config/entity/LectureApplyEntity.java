package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lecture_apply")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class LectureApplyEntity extends BaseEntity{
    /** PK **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long ilecture;

    /** FK **/
    @ManyToOne
    @JoinColumn(name = "ilectureName" )
    @ToString.Exclude
    private LectureNameEntity lectureNameEntity;

    /** FK **/
    @ManyToOne
    @JoinColumn(name = "ilectureRoom")
    @ToString.Exclude
    private LectureRoomEntity lectureRoomEntity;

    /** FK **/
    @ManyToOne
    @JoinColumn(name = "iprofessor")
    @ToString.Exclude
    private ProfessorEntity professorEntity;

    /** FK **/
    @ManyToOne
    @JoinColumn(name = "isemester")
    @ToString.Exclude
    private SemesterEntity semesterEntity;


    @Column(name = "opening_proceudres")
    @ColumnDefault("1")
    private Integer openingProceudres;


    @Column(name = "attendance", length = 10)
    private int attendance;

    @Column(name = "midterm_examination", length = 10)
    private int midtermExamination;

    @Column(name = "final_examination", length = 10)
    private int finalExamination;

    @Column(name = "lecture_max_people",nullable = false, length = 10)
    private int lectureMaxPeople;

    @Column(name = "grade_limit", length = 10, nullable = false)
    private int gradeLimit;

    @Column(name = "del_yn", length = 10)
    @ColumnDefault("0")
    private int delYn;

    @Column(length = 1000)
    private String ctnt;

    @Column(length = 80)
    private String textbook;

    @Column
    private String bookUrl;

    @Column(name = "students_apply_deadline")
    private LocalDate studentsApplyDeadline;

    @OneToOne(mappedBy ="lectureApplyEntity" )
    @ToString.Exclude
    private LectureScheduleEntity lectureScheduleEntity;

    @OneToMany(mappedBy = "lectureApplyEntity")
    @ToString.Exclude
    private List<LectureStudentEntity> lectureStudentEntity;
}
