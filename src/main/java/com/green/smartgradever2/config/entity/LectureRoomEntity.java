package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "lecture_room")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@DynamicInsert
public class LectureRoomEntity {

    /** pk값 **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long ilectureRoom;

    /** 강의실 **/
    @Column(name = "lecture_room_name", length = 50, nullable = false)
    private String lectureRoomName;

    /** 건물 **/
    @Column(name = "building_name", length = 50, nullable = false)
    private String buildingName;

    /** 수용인원 **/
    @Column(name = "max_capacity", length = 10, nullable = false)
    private int maxCapacity;

    /** 삭제 여부 **/
    @Column(name = "del_yn", length = 10,nullable = false)
    private int delYn;
}
