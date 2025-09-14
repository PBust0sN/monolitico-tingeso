package com.example.monolitico.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NewLoanDTO {
    private Long staff_id;
    private Long client_id;
    private List<Long> tools_id;
    private Long days;
}
