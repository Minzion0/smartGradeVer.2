package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "lecture_name")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class LectureNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ilecture_name",updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long ilectureName;

    @Column(name = "lecture_name", nullable = false, length = 50)
    private String lectureName;

    @Column(nullable = false, length = 10)
    private int score;

    @Column(name = "del_yn", length = 10)
    @ColumnDefault("0")
    private int delYn;


}
