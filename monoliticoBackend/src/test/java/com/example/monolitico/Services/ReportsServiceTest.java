package com.example.monolitico.Services;

import com.example.monolitico.Entities.*;
import com.example.monolitico.Repositories.ReportsRepository;
import com.example.monolitico.Service.ClientService;
import com.example.monolitico.Service.LoansService;
import com.example.monolitico.Service.ReportsServices;
import com.example.monolitico.Service.ToolsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportsServiceTest {

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private LoansService loansService;

    @Mock
    private ClientService clientService;

    @Mock
    private ToolsService toolsService;

    @InjectMocks
    private ReportsServices reportsServices;

    private ReportsEntity report;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        report = new ReportsEntity();
        report.setReportId(1L);
        report.setReportDate(Date.valueOf(LocalDate.of(2025, 10, 14)));
        report.setLoanIdReport(true);
        report.setToolsIdRanking(false);
        report.setFineIdReports(true);
        report.setClientIdBehind(false);
        report.setClientIdReport(10L);
    }

    // =================== CRUD Tests ===================

    @Test
    void testGetAllReports() {
        when(reportsRepository.findAll()).thenReturn(Collections.singletonList(report));

        List<ReportsEntity> result = reportsServices.getAllReports();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reportsRepository, times(1)).findAll();
    }

    @Test
    void testGetReportsById() {
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report));

        ReportsEntity result = reportsServices.getReportsById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getReportId());
        verify(reportsRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveReport() {
        when(reportsRepository.save(any(ReportsEntity.class))).thenReturn(report);

        ReportsEntity result = reportsServices.saveReport(report);

        assertNotNull(result);
        assertEquals(report.getReportId(), result.getReportId());
        verify(reportsRepository, times(1)).save(report);
    }

    @Test
    void testUpdateReport() {
        when(reportsRepository.save(any(ReportsEntity.class))).thenReturn(report);

        ReportsEntity result = reportsServices.updateReport(report);

        assertNotNull(result);
        assertEquals(report.getReportId(), result.getReportId());
        verify(reportsRepository, times(1)).save(report);
    }

    @Test
    void testDeleteReport_Success() throws Exception {
        doNothing().when(reportsRepository).deleteById(1L);

        boolean result = reportsServices.deleteReport(1L);

        assertTrue(result);
        verify(reportsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteReport_Exception() {
        doThrow(new RuntimeException("DB error")).when(reportsRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> reportsServices.deleteReport(1L));
        assertTrue(exception.getMessage().contains("DB error"));
        verify(reportsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetReportsBetweenDates() {
        Date startDate = Date.valueOf("2025-10-01");
        Date endDate = Date.valueOf("2025-10-31");

        when(reportsRepository.findByReportDateBetween(startDate, endDate))
                .thenReturn(Collections.singletonList(report));

        List<ReportsEntity> result = reportsServices.getReportsBetweenDates(startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reportsRepository, times(1)).findByReportDateBetween(startDate, endDate);
    }

    // =================== Empty Report Methods ===================
    @Test
    void testGenerateLoansReport() {
        List<LoansEntity> result = reportsServices.generateLoansReport();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGenerateBehindClientsReport() {
        List<ClientEntity> result = reportsServices.generateBehindClientsReport();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGeneratedMostLoanToolsReport() {
        List<ToolsEntity> result = reportsServices.generatedMostLoanToolsReport();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =================== Edge Cases ===================
    @Test
    void testGetReportsById_NotFound() {
        when(reportsRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> reportsServices.getReportsById(99L));
        verify(reportsRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveReport_DateSet() {
        ReportsEntity newReport = new ReportsEntity();
        newReport.setLoanIdReport(false);

        when(reportsRepository.save(any(ReportsEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReportsEntity saved = reportsServices.saveReport(newReport);

        assertNotNull(saved.getReportDate());
        verify(reportsRepository, times(1)).save(newReport);
    }

    @Test
    void testGetAllByClientId() {
        when(reportsRepository.findByClientIdReport(10L))
                .thenReturn(Collections.singletonList(report));

        List<ReportsEntity> result = reportsServices.getAllByClientId(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reportsRepository, times(1)).findByClientIdReport(10L);
    }

    @Test
    void testSaveReport_OverridesExistingDate() {
        ReportsEntity old = new ReportsEntity();
        old.setReportDate(Date.valueOf("2000-01-01"));

        when(reportsRepository.save(any(ReportsEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ReportsEntity saved = reportsServices.saveReport(old);

        assertNotEquals(Date.valueOf("2000-01-01"), saved.getReportDate());
        assertNotNull(saved.getReportDate());
        verify(reportsRepository, times(1)).save(old);
    }

}
