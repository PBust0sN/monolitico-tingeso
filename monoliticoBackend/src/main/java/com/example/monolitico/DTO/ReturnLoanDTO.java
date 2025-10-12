package com.example.monolitico.DTO;

import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Entities.LoansEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ReturnLoanDTO {
    private Long repoAmount;
    private Long fineAmount;
    private LoansEntity loan;
    private FineEntity fine;
    private FineEntity RepoFine;
}
