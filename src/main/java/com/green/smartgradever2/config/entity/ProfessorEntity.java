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
@Table(name = "professor")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class ProfessorEntity extends BaseEntity {


    //** 교수pk값 **/
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long iprofessor;

    //** 전공pk값 **/
    @ManyToOne
    @JoinColumn(name = "imajor")
    @ToString.Exclude
    private MajorEntity majorEntity;

    //** 비밀번호 **/
    @Column(name = "professor_password", length = 100)
    private String professorPassword;

    //** 이름 **/
    @Column(length = 15)
    private String nm;

    //** 성별 **/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    //** 사진 **/
    @Column(length = 100, nullable = true)
    private String pic;

    //** 생년월일 **/
    @Column(name = "birthdate", columnDefinition = "date")
    private LocalDate birthDate;

    //** 폰번호 **/
    @Column(length = 15, nullable = true)
    private String phone;

    //** 이메일 **/
    @Column(length = 50, nullable = true,unique = true)
    private String email;

    //** 주소 **/
    @Column(length = 100, nullable = true)
    private String address;

    //** 롤값 ROLE_PROFESSOR **/
   @Column()
    @ColumnDefault( "'ROLE_PROFESSOR'")
    private String role;

    /** 시크릿키 **/
    @Column(name = "secret_key",length = 100, nullable = true)
    private String secretKey;

    /** Opt QR URL 저장 **/
    @Column(name = "otp_url")
    private String otpUrl;
}
