package com.example.monolitico.Services;

import com.example.monolitico.Entities.ToolsReportEntity;
import com.example.monolitico.Repositories.ToolsReportRepository;
import com.example.monolitico.Service.ToolsReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsReportServiceTest {

    @Mock
    private ToolsReportRepository toolsReportRepository;

    @InjectMocks
    private ToolsReportService toolsReportService;

    private ToolsReportEntity toolReport;

    @BeforeEach
    void setUp() {
        toolReport = new ToolsReportEntity();
        toolReport.setToolIdReport(1L);
        toolReport.setToolName("Taladro");
        toolReport.setCategory("Eléctrico");
        toolReport.setLoanCount(15L);
    }

    @Test
    void testGetByToolId_Success() {
        when(toolsReportRepository.findById(1L)).thenReturn(Optional.of(toolReport));

        ToolsReportEntity result = toolsReportService.getByToolId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getToolIdReport());
        assertEquals("Taladro", result.getToolName());
        assertEquals("Eléctrico", result.getCategory());
        assertEquals(15L, result.getLoanCount());
        verify(toolsReportRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByToolId_NotFound() {
        when(toolsReportRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            toolsReportService.getByToolId(2L);
        });

        verify(toolsReportRepository, times(1)).findById(2L);
    }

    @Test
    void testCreateToolReport_Success() {
        when(toolsReportRepository.save(any(ToolsReportEntity.class))).thenReturn(toolReport);

        ToolsReportEntity result = toolsReportService.createToolReport(toolReport);

        assertNotNull(result);
        assertEquals("Taladro", result.getToolName());
        assertEquals("Eléctrico", result.getCategory());
        verify(toolsReportRepository, times(1)).save(toolReport);
    }
}

