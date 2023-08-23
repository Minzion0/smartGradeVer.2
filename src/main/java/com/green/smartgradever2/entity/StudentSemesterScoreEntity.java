package com.green.smartgradever2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "student_semester_score")
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)

@DynamicInsert
public class StudentSemesterScoreEntity {
}
