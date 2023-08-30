package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "major")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class MajorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long imajor;

    @Column(name = "major_name", nullable = false, length = 50, unique = true)
    private String majorName;

    @Column(name = "graduation_score", nullable = false, length = 10)
    private int graduationScore;

    @Column(name = "del_yn", length = 10)
    @ColumnDefault("0")
    private int delYn;

    @Column(name = "remarks", length = 50)
    private String remarks;


}
