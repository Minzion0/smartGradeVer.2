package com.green.smartgradever2.entity;

import com.green.smartgradever2.entity.model.GenderEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class StudentEntity extends BaseEntity {
    @Id
    @Column(updatable = false,nullable = false,columnDefinition = "INT UNSIGNED")
    @Size(max = 10)
    private int studentNum;

    @Column(name = "student_password",nullable = false,length = 100)
    private String studentPassword;


    @JoinColumn(name = "imajor")
    @ManyToOne
    @ToString.Exclude
    private MajorEntity majorEntity;

    @Column(name = "grade",nullable = false,length = 10,columnDefinition = "1 DEFAULT")
    private Integer grade;

    @Column(nullable = false,length = 10)
    private String nm;

    @Column(nullable = false)
    private GenderEnum gender;


    @Column(length = 100)
    private String pic;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column
    private String phone;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String address;

    @Column(name = "finished_yn",columnDefinition = "1 DEFAULT")
    private Integer finishedYn;

    @Column(columnDefinition = "ROLE_STUDENT DEFAULT")
    private String role;

    @Column(name = "secret_key")
    private String secretKey;




}
