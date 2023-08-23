package com.green.smartgradever2.entity;

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
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class LectureRoomEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ilecture_room", updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long ilectureRoom;


    @Column(name = "lecture_room_name", length = 50, nullable = false)
    private String lectureRoomName;


    @Column(name = "building_name", length = 50, nullable = false)
    private String buildingName;


    @Column(name = "max_capacity", length = 10, nullable = false)
    private int maxCapacity;
}
