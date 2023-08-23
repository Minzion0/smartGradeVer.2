package com.green.smartgradever2.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "semester")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class SemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long isemester;

    @Column(name = "year")
    private LocalDate year;

    @Column(name = "semester",length = 10, nullable = false)
    private int semester;

    @Column(name = "semester_str_date", nullable = false)
    private LocalDate semesterStrDate;

    @Column(name = "semester_end_date", nullable = false)
    private LocalDate semesterEndDate;

    @Column(name = "del_yn", length = 10)
    private int delYn;

    @Column(name = "lecture_apply_deadline", nullable = false)
    private LocalDate lectureApplyDeadline;
}
