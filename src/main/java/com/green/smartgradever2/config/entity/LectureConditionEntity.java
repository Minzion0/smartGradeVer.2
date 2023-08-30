package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

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
    private Long icodition;

    /** lecture_applly 외래키 **/
    @OneToOne
    @MapsId
    @JoinColumn(name = "ilecture", columnDefinition = "BIGINT UNSIGNED")
    @ToString.Exclude
    private LectureApplyEntity ilecture;

    /** 신청반려 내용작성 **/
    @Column(name = "return_ctnt", nullable = false)
    private String returnCtnt;

    /** 반려일시 **/
    @Column(name = "return_date")
    @CreationTimestamp
    private LocalDateTime returnDate;

}
