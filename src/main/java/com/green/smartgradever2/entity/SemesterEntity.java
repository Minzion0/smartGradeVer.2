package com.green.smartgradever2.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "major")
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

    @Column(name = "semester",length = 10)
    private int semester;

    @Column(name = "semester_str_date")
    private LocalDate semesterStrDate;

    @Column(name = "semester_end_date")
    private LocalDate semesterEndDate;

    @Column(name = "del_yn", length = 10)
    private int delYn;
}
