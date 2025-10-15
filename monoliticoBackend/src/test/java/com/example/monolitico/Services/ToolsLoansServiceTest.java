package com.example.monolitico.Services;

import com.example.monolitico.Entities.ToolsLoansEntity;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import com.example.monolitico.Service.ToolsLoansService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolsLoansServiceTest {

    @Mock
    private ToolsLoansRepository toolsLoansRepository;

    @InjectMocks
    private ToolsLoansService toolsLoansService;

    private ToolsLoansEntity toolsLoansEntity;

    @BeforeEach
    void setUp() {
        toolsLoansEntity = new ToolsLoansEntity();
        toolsLoansEntity.setId(1L);
        toolsLoansEntity.setToolId(100L);
        toolsLoansEntity.setLoanId(200L);
    }

    @Test
    void testGetAllToolsLoans() {
        when(toolsLoansRepository.findAll()).thenReturn(List.of(toolsLoansEntity));

        List<ToolsLoansEntity> result = toolsLoansService.getAllToolsLoans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(toolsLoansEntity, result.get(0));
        verify(toolsLoansRepository, times(1)).findAll();
    }

    @Test
    void testSaveToolsLoans() {
        when(toolsLoansRepository.save(any(ToolsLoansEntity.class))).thenReturn(toolsLoansEntity);

        ToolsLoansEntity saved = toolsLoansService.saveToolsLoans(toolsLoansEntity);

        assertNotNull(saved);
        assertEquals(toolsLoansEntity.getId(), saved.getId());
        verify(toolsLoansRepository, times(1)).save(toolsLoansEntity);
    }

    @Test
    void testGetToolsLoansById() {
        when(toolsLoansRepository.findById(1L)).thenReturn(Optional.of(toolsLoansEntity));

        ToolsLoansEntity result = toolsLoansService.getToolsLoansById(1L);

        assertNotNull(result);
        assertEquals(toolsLoansEntity.getId(), result.getId());
        verify(toolsLoansRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateToolsLoans() {
        when(toolsLoansRepository.save(any(ToolsLoansEntity.class))).thenReturn(toolsLoansEntity);

        ToolsLoansEntity updated = toolsLoansService.updateToolsLoans(toolsLoansEntity);

        assertNotNull(updated);
        assertEquals(toolsLoansEntity.getId(), updated.getId());
        verify(toolsLoansRepository, times(1)).save(toolsLoansEntity);
    }

    @Test
    void testDeleteToolsLoans() throws Exception {
        doNothing().when(toolsLoansRepository).deleteById(1L);

        boolean result = toolsLoansService.deleteToolsLoans(1L);

        assertTrue(result);
        verify(toolsLoansRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetToolsIDsByLoanId() {
        when(toolsLoansRepository.findByLoanId(200L)).thenReturn(List.of(100L, 101L));

        List<Long> result = toolsLoansService.getToolsIDsByLoanId(200L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(List.of(100L, 101L), result);
        verify(toolsLoansRepository, times(1)).findByLoanId(200L);
    }

    @Test
    void testGetLoansIDsByToolId() {
        when(toolsLoansRepository.findByToolId(100L)).thenReturn(List.of(200L, 201L));

        List<Long> result = toolsLoansService.getLoansIDsByToolId(100L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(List.of(200L, 201L), result);
        verify(toolsLoansRepository, times(1)).findByToolId(100L);
    }
}
