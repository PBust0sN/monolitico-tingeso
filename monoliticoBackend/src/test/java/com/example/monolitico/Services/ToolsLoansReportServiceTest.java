package com.example.monolitico.Services;

import com.example.monolitico.Entities.ToolsLoanReportEntity;
import com.example.monolitico.Repositories.ToolsLoanReportRepository;
import com.example.monolitico.Service.ToolsLoanReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsLoansReportServiceTest {

    @Mock
    private ToolsLoanReportRepository toolsLoanReportRepository;

    @InjectMocks
    private ToolsLoanReportService toolsLoanReportService;

    private ToolsLoanReportEntity reportEntity;

    @BeforeEach
    void setUp() {
        reportEntity = new ToolsLoanReportEntity();
        reportEntity.setToolLoanReportId(1L);
        reportEntity.setLoanId(100L);
        reportEntity.setToolId(200L);
    }

    @Test
    void testCreateToolsLoanReport() {
        when(toolsLoanReportRepository.save(any(ToolsLoanReportEntity.class))).thenReturn(reportEntity);

        ToolsLoanReportEntity saved = toolsLoanReportService.createToolsLoanReport(reportEntity);

        assertNotNull(saved);
        assertEquals(reportEntity.getToolLoanReportId(), saved.getToolLoanReportId());
        assertEquals(reportEntity.getLoanId(), saved.getLoanId());
        assertEquals(reportEntity.getToolId(), saved.getToolId());
        verify(toolsLoanReportRepository, times(1)).save(reportEntity);
    }

    @Test
    void testFindByLoanId() {
        List<Long> toolIds = List.of(200L, 201L, 202L);
        when(toolsLoanReportRepository.findToolIdByLoanId(100L)).thenReturn(toolIds);

        List<Long> result = toolsLoanReportService.findByLoanId(100L);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(toolIds, result);
        verify(toolsLoanReportRepository, times(1)).findToolIdByLoanId(100L);
    }
}
