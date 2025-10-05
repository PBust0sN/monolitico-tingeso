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
    @Column(unique = true, nullable = false)
    private Long reportId;

    private Date reportDate;
    private Boolean loanIdReport;
    private Boolean toolsIdRanking;
    private Boolean fineIdReports;
    private Boolean clientIdBehind;
    private Long clientIdReport;
}

