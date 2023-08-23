package com.green.smartgradever2.entity;

import com.green.smartgradever2.entity.model.GenderEnum;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(length = 100, nullable = true)
    private String pic;

    @Column(name = "birthdate", columnDefinition = "date")
    private LocalDate birthDate;

    @Column(length = 15, nullable = true)
    private String phone;

    @Column(length = 50, nullable = true)
    private String email;

    @Column(length = 100, nullable = true)
    private String address;

    @Column(length = 30, nullable = false,columnDefinition = "VARCHAR(30) DEFAULT 'ROLE_PROFESSOR'")
    private String role;

    @Column(name = "secret_key",length = 100, nullable = true)
    private String secretKey;
}
