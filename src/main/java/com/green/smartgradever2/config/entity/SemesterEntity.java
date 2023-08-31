package com.green.smartgradever2.config.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.repository.Temporal;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Entity
@Table(name = "semester"
        , uniqueConstraints = {@UniqueConstraint(
                name = "uniqu"
                , columnNames = {
                    "year"
                , "semester"
        })
})
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class SemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long isemester;

    @Column(name = "year", columnDefinition = "YEAR")
    private int year;

    @Column(name = "semester", length = 10, nullable = false)
    private int semester;

    @Column(name = "semester_str_date", nullable = false)
    private LocalDate semesterStrDate;

    @Column(name = "semester_end_date", nullable = false)
    private LocalDate semesterEndDate;

    @Column(name = "del_yn", length = 10)
    @ColumnDefault("0")
    private int delYn;

    @Column(name = "lecture_apply_deadline", nullable = false)
    private LocalDate lectureApplyDeadline;


//    @OneToMany(mappedBy = "studentNum")
//    @ToString.Exclude
//    private List<StudentEntity> studentEntity;
}
