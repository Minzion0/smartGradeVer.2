package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.orm.jpa.vendor.Database;

import java.time.LocalDate;

@Entity
@Table(name = "professor")
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProfessorEntity extends BaseEntity {

    @Id  //교수 pk값
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long iprofessor;

//    @ManyToOne
//    @JoinColumn(name = "imajor")
//    @ToString.Exclude
//    private

    @Column(length = 100)
    private String professorpassword;

    @Column(length = 15)
    private String nm;

    @Column(length = 1, columnDefinition = "char")
    private String gender;

    @Column(length = 100, nullable = true)
    private String pic;

    @Column(length = 100,columnDefinition = "date")
    private LocalDate birthdate;

    @Column(length = 15,)



}
