package com.example.monolitico.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "TOOLS_LOAN_REPORT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ToolsLoanReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long toolLoanReportId;

    private Long loanId;
    private Long toolId;
}