package com.example.monolitico.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CalculateCostDTO {
    private Long repoAmount;
    private Long fineAmount;
}
