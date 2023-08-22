package com.green.smartgradever2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Table(name = "admin")
@Entity
@ToString(callSuper = true)
public class AdminEntity{

    /** pk **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long iadmin;

    /** admin ID **/
    @Column(nullable = false, length = 10, name = "admin_id")
    private String adminId;

    /** admin password **/
    @Column(nullable = false, length = 100, name = "admin_password")
    private String adminPassword;

    /** admin role **/
    @Column(nullable = false, length = 30)
    private String role;

    /** admin secret_key **/
    @Column(length = 100, name = "secret_key")
    private String secretKey;
}
