package com.example.monolitico.Services;

import com.example.monolitico.Entities.FineReportEntity;
import com.example.monolitico.Repositories.FineReportRepository;
import com.example.monolitico.Service.FineReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FineReportServiceTest {

    @Mock
    private FineReportRepository fineReportRepository;

    @InjectMocks
    private FineReportService fineReportService;

    private FineReportEntity fineReportEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fineReportEntity = new FineReportEntity();
        fineReportEntity.setFineReportId(1L);
        fineReportEntity.setReportId(100L);
        fineReportEntity.setAmount(5000L);
        fineReportEntity.setType("Retraso");
        fineReportEntity.setClientId(10L);
        fineReportEntity.setLoanId(20L);
        fineReportEntity.setState("Pendiente");
        fineReportEntity.setDate(new Date(System.currentTimeMillis()));
    }

    @Test
    void testGetAllFineReport() {
        when(fineReportRepository.findAll()).thenReturn(Arrays.asList(fineReportEntity));

        List<FineReportEntity> result = fineReportService.getAllFineReport();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFineReportId());
        verify(fineReportRepository, times(1)).findAll();
    }

    @Test
    void testGetFineReportsByReportId() {
        when(fineReportRepository.findByReportId(100L)).thenReturn(Arrays.asList(fineReportEntity));

        List<FineReportEntity> result = fineReportService.getFineReportsByReportId(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getReportId());
        verify(fineReportRepository, times(1)).findByReportId(100L);
    }

    @Test
    void testCreateFineReport() {
        when(fineReportRepository.save(any(FineReportEntity.class))).thenReturn(fineReportEntity);

        FineReportEntity result = fineReportService.createFineReport(fineReportEntity);

        assertNotNull(result);
        assertEquals(fineReportEntity.getFineReportId(), result.getFineReportId());
        assertEquals("Retraso", result.getType());
        verify(fineReportRepository, times(1)).save(fineReportEntity);
    }

    @Test
    void testDeleteFineReportById_Success() throws Exception {
        doNothing().when(fineReportRepository).deleteById(1L);

        boolean result = fineReportService.deleteFineReportById(1L);

        assertTrue(result);
        verify(fineReportRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFineReportById_ThrowsException() {
        doThrow(new RuntimeException("DB error")).when(fineReportRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> {
            fineReportService.deleteFineReportById(1L);
        });

        assertTrue(exception.getMessage().contains("DB error"));
        verify(fineReportRepository, times(1)).deleteById(1L);
    }
}
