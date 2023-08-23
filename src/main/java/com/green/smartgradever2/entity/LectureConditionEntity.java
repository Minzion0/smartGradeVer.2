package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Table(name = "lecture_condition")
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LectureConditionEntity extends BaseEntity{

    /** PK **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long icodition;

    /** lecture_applly 외래키 **/
    @ManyToOne
    @JoinColumn(name = "ilecture")
    @ToString.Exclude
    private LectureAppllyEntity ilecture;

    /** 신청반려 내용작성 **/
    @Column(name = "return_ctnt", nullable = false)
    private String returnCtnt;

    /** 반려일시 **/
    @Column(name = "return_date")
    private LocalDateTime returnDate;

}
