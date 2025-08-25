package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "LOANS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class LoansEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long loan_id;

    private Date delivery_date;
    private Date return_date;
    private String loan_type;
    private Date date;
    private String user_responsable;
}
