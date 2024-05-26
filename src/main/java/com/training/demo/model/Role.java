package com.training.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements AnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long     id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}