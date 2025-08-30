package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "CLIENT_LOANS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ClientLoansEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClientLoans;

    private Long clientId;
    private Long loanId;
}
