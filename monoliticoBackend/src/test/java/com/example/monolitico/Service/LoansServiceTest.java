package com.example.monolitico.Service;

import com.example.monolitico.DTO.ReturnLoanDTO;
import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoansServiceTest {

    @Mock
    private LoansRepository loansRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private FineService fineService;

    @Mock
    private ToolsService toolsService;

    @Mock
    private ToolsLoansRepository toolsLoansRepository;

    @Mock
    private ToolsLoansService toolsLoansService;

    @Mock
    private RecordsServices recordsServices;

    @InjectMocks
    private LoansService loansService;

    @Test
    public void checkDates_returns_true_for_future_returnDate() {
        LoansEntity loan = new LoansEntity();
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));

        assertTrue(loansService.checkDates(loan));
    }

    @Test
    public void checkDates_returns_false_for_past_returnDate() {
        LoansEntity loan = new LoansEntity();
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(3)));

        assertFalse(loansService.checkDates(loan));
    }

    @Test
    public void calculateFine_createsFine_when_loan_is_behind() {
        Long loanId = 100L;

        LoansEntity loan = new LoansEntity();
        loan.setLoanId(loanId);
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(2))); // behind by 2 days

        when(loansRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(toolsLoansRepository.findByLoanId(loanId)).thenReturn(List.of(1L));

        ToolsEntity tool = new ToolsEntity();
        tool.setToolId(1L);
        tool.setDiaryFineFee(10L);
        when(toolsService.getToolsById(1L)).thenReturn(tool);

        doNothing().when(fineService).saveFine(any(FineEntity.class));

        ReturnLoanDTO dto = loansService.calculateFine(loanId, 50L);

        // fine should be > 0 (2 days * 10)
        assertNotNull(dto);
        assertTrue(dto.getFineAmount() >= 20L);
        verify(fineService, atMostOnce()).saveFine(any(FineEntity.class));
    }

    @Test
    public void calculateRepoFine_createsRepoFine_and_records_for_damaged_tools() {
        Long loanId = 200L;
        Long clientId = 5L;

        when(toolsLoansRepository.findByLoanId(loanId)).thenReturn(List.of(11L));

        ToolsEntity damaged = new ToolsEntity();
        damaged.setToolId(11L);
        damaged.setInitialState("Dañada");
        damaged.setRepositionFee(150L);

        when(toolsService.getToolsById(11L)).thenReturn(damaged);

        doNothing().when(recordsServices).saveRecord(any());
        doNothing().when(fineService).saveFine(any(FineEntity.class));

        ReturnLoanDTO dto = loansService.calculateRepoFine(loanId, clientId);

        assertNotNull(dto);
        assertEquals(150L, dto.getRepoAmount());
        assertNotNull(dto.getRepoFine());
        verify(recordsServices, atLeastOnce()).saveRecord(any());
        verify(fineService, atLeastOnce()).saveFine(any(FineEntity.class));
    }

    @Test
    public void reamaningDaysOnLoan_returns_correct_difference() {
        Long loanId = 300L;
        LoansEntity loan = new LoansEntity();
        loan.setLoanId(loanId);
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(7)));

        when(loansRepository.findById(loanId)).thenReturn(Optional.of(loan));

        long days = loansService.reamaningDaysOnLoan(loanId);

        assertTrue(days >= 6 && days <= 8); // allow small timing differences
    }
}
