package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Table(name = "board_pic")
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BoardPicEntity extends BaseEntity{

    /** pk **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long ipic;

    /** pic **/
    @Column(name = "pic")
    private String pic;

    /** 외래키 iboard **/
    @ManyToOne
    @JoinColumn(name = "iboard")
    @ToString.Exclude
    private BoardEntity boardEntity;
}
