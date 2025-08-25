package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "RECORDS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class RecordsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long record_id;

    private Date record_date;
    private String description;
    private String record_type;
    private Double record_amount;
}
