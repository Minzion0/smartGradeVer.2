package com.green.smartgradever2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "student")
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
public class StudentEntity extends BoardEntity {
    @Id
    @Column(updatable = false,nullable = false,columnDefinition = "INT UNSIGNED")
    @Size(max = 10)
    private int studentNum;

    @Column
    private String student



}
