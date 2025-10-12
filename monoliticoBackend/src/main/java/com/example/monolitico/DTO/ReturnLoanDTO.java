package com.example.monolitico.DTO;

import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Entities.LoansEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnLoanDTO {
    private Long repoAmount;
    private Long fineAmount;
    private LoansEntity loan;
    private FineEntity fine;
    private FineEntity RepoFine;
    private List<String> tools;
    private Long lowDmgAmount;
}
