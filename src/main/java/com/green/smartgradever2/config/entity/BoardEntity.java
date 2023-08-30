package com.green.smartgradever2.config.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Data
@Table (name = "board")
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BoardEntity extends BaseEntity{

    /** PK **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long iboard;

    /** admin table 외래키 **/
    @ManyToOne
    @JoinColumn(name = "iadmin")
    @ToString.Exclude
    private AdminEntity adminEntity;

    /** 제목 **/
    @Column(nullable = false, length = 100)
    private String title;

    /** 내용 **/
    @Column(nullable = false, length = 1000)
    private String ctnt;

    /** 중요도 **/
    @Column(length = 2)
    @ColumnDefault("0")
    private int importance;

    /** 조회수 **/
    @Column(columnDefinition = "BIGINT UNSIGNED", name = "board_view")
    private Long boardView;
}
