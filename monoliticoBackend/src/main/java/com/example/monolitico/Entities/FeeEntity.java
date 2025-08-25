package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "FEE")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long fee_id;

    private String fee_type;
    private Double amount;
    private Long extra_charges;
    private String description;
}
