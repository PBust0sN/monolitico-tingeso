package com.example.monolitico.Services;

import com.example.monolitico.Entities.LoansReportEntity;
import com.example.monolitico.Repositories.LoanReportRepository;
import com.example.monolitico.Service.LoansReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoansReportServiceTest {

    @Mock
    private LoanReportRepository loanReportRepository;

    @InjectMocks
    private LoansReportService loansReportService;

    private LoansReportEntity report;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        report = new LoansReportEntity();
        report.setLoanReportId(1L);
        report.setReportId(100L);
        report.setClientIdBehind(10L);
        report.setDeliveryDate(Date.valueOf("2025-10-14"));
        report.setReturnDate(Date.valueOf("2025-10-20"));
        report.setLoanType("loan");
        report.setDate(Date.valueOf("2025-10-14"));
        report.setStaffId(5L);
        report.setClientId(20L);
        report.setAmount(5000L);
        report.setExtraCharges(0L);
    }

    @Test
    void testGetAllLoansReport() {
        when(loanReportRepository.findAll()).thenReturn(Arrays.asList(report));

        List<LoansReportEntity> result = loansReportService.getAllLoansReport();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(loanReportRepository, times(1)).findAll();
    }

    @Test
    void testGetLoansReportByReportId() {
        when(loanReportRepository.findByReportId(100L)).thenReturn(Arrays.asList(report));

        List<LoansReportEntity> result = loansReportService.getLoansReportByReportId(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getReportId());
        verify(loanReportRepository, times(1)).findByReportId(100L);
    }

    @Test
    void testGetLoansReportById() {
        when(loanReportRepository.getById(1L)).thenReturn(report);

        LoansReportEntity result = loansReportService.getLoansReportById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getLoanReportId());
        verify(loanReportRepository, times(1)).getById(1L);
    }

    @Test
    void testCreateLoansReport() {
        when(loanReportRepository.save(any(LoansReportEntity.class))).thenReturn(report);

        LoansReportEntity result = loansReportService.createLoansReport(report);

        assertNotNull(result);
        assertEquals(report.getLoanReportId(), result.getLoanReportId());
        verify(loanReportRepository, times(1)).save(report);
    }

    @Test
    void testDeleteLoansReportById_Success() throws Exception {
        doNothing().when(loanReportRepository).deleteById(1L);

        boolean result = loansReportService.deleteLoansReportById(1L);

        assertTrue(result);
        verify(loanReportRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLoansReportById_Exception() {
        doThrow(new RuntimeException("DB error")).when(loanReportRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> loansReportService.deleteLoansReportById(1L));
        assertTrue(exception.getMessage().contains("DB error"));
        verify(loanReportRepository, times(1)).deleteById(1L);
    }
}
