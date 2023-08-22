package com.green.smartgradever2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table (name = "board")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardEntity {

    @Id
    private Long iboard;
}
