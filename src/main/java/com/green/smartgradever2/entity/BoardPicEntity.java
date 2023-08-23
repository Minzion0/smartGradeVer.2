package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Table(name = "board_pic")
@Entity
@ToString(callSuper = true)
public class BoardPicEntity {

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
    private BoardEntity iboard;
}
