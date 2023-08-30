package com.green.smartgradever2.config.entity;

import com.green.smartgradever2.config.entity.model.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class StudentEntity extends BaseEntity {
    @Id
    @Column(updatable = false,nullable = false,columnDefinition = "INT UNSIGNED")
    private Integer studentNum;

    @Column(name = "student_password",nullable = false,length = 100)
    private String studentPassword;


    @JoinColumn(name = "imajor")
    @ManyToOne
    @ToString.Exclude
    private MajorEntity majorEntity;

    @JoinColumn(name = "isemester")
    @ManyToOne
    @ToString.Exclude
    private SemesterEntity semesterEntity;


    @Column(name = "grade",length = 10)
    @ColumnDefault("1")
    private Integer grade;

    @Column(nullable = false,length = 10)
    private String nm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;


    @Column(length = 100)

    private String pic;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(length = 13)
    @Size(min = 13)
    private String phone;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String address;

    @Column(name = "finished_yn")
    @ColumnDefault("1")
    private Integer finishedYn;

    @Column()
    @ColumnDefault( "'ROLE_STUDENT'")
    private String role;

    @Column(name = "secret_key")
    private String secretKey;





}
