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
    private Long recordId;

    private Date recordDate;
    private String description;
    private String recordType;
    private Double recordAmount;
    private Long clientId;
    private Long loanId;
}
