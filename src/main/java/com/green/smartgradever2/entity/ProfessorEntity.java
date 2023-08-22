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

    @ManyToOne
    @JoinColumn(name = "imajor")
    @ToString.Exclude
    private MajorEntity majorEntity;

    @Column(name = "professor_password", length = 100)
    private String professorPassword;

    @Column(length = 15)
    private String nm;

    @Column(length = 1, columnDefinition = "char")
    private String gender;

    @Column(length = 100, nullable = true)
    private String pic;

    @Column(name = "birthdate", length = 100, columnDefinition = "date")
    private LocalDate birthDate;

    @Column(length = 15, nullable = true)
    private String phone;

    @Column(length = 50, nullable = true)
    private String email;

    @Column(length = 100, nullable = true)
    private String address;

    @Column(length = 30, nullable = false)
    private String role;
    @Column(name = "secret_key",length = 100, nullable = true)
    private String secretKey;
}
