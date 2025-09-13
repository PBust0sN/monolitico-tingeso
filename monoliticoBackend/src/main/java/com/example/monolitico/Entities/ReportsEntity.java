package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Table(name = "REPORTS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ReportsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportsId;

    private Date reportDate;

    @Column(columnDefinition = "TEXT")
    private String reportContent;
}
