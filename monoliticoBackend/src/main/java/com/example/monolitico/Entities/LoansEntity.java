package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Table(name = "LOANS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class LoansEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long loanId;

    private LocalDate deliveryDate;
    private LocalDate returnDate;
    private String loanType;
    private LocalDate date;
    private Long staffId;
    private Long clientId;
    private Long amount;
    private Long extraCharges;
    private Boolean active;
}
