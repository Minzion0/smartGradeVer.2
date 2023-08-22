package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "admin")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long iadmin;

    @Column(nullable = false, length = 10)
    private String adminId;

    @Column(nullable = false, length = 100)
    private String adminPassword;

    @Column(length = 2)
    @Size(min = 0, max = 1)
    private int delYn;

    @Column(length = 30)
    private String role;

    @Column(length = 100)
    private String secretKey;
}
