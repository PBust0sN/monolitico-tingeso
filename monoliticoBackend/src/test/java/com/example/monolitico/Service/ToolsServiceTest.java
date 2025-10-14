package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.ToolsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToolsServiceTest {

    @Mock
    private ToolsRepository toolsRepository;

    @Mock
    private RecordsServices recordservice;

    @Mock
    private StaffService staffService;

    @InjectMocks
    private ToolsService toolsService;

    @Test
    public void hasDisponibilityAndStock_true_when_disponible_and_stock_positive() {
        ToolsEntity tool = new ToolsEntity();
        tool.setToolId(1L);
        tool.setDisponibility("Disponible");
        tool.setStock(2L);

        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));

        boolean result = toolsService.hasDisponibilityAndStock(1L);

        assertTrue(result);
        verify(toolsRepository, times(1)).findById(1L);
    }

    @Test
    public void hasDisponibilityAndStock_false_when_not_disponible_or_no_stock() {
        ToolsEntity tool = new ToolsEntity();
        tool.setToolId(2L);
        tool.setDisponibility("No disponible");
        tool.setStock(0L);

        when(toolsRepository.findById(2L)).thenReturn(Optional.of(tool));

        boolean result = toolsService.hasDisponibilityAndStock(2L);

        assertFalse(result);
        verify(toolsRepository, times(1)).findById(2L);
    }

    @Test
    public void updateStockById_increments_stock_and_saves() {
        ToolsEntity tool = new ToolsEntity();
        tool.setToolId(3L);
        tool.setStock(1L);

        when(toolsRepository.findById(3L)).thenReturn(Optional.of(tool));
        when(toolsRepository.save(any(ToolsEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ToolsEntity updated = toolsService.updateStockById(3L);

        assertNotNull(updated);
        assertEquals(2L, updated.getStock());
        verify(toolsRepository, times(1)).findById(3L);
        verify(toolsRepository, times(1)).save(any(ToolsEntity.class));
    }

    @Test
    public void saveTool_when_valid_saves_and_generates_record() {
        ToolsEntity input = new ToolsEntity();
        input.setToolName("Taladro");
        input.setCategory("Herramienta");
        input.setRepositionFee(100L);

        ToolsEntity saved = new ToolsEntity();
        saved.setToolId(10L);
        saved.setToolName("Taladro");

        when(toolsRepository.save(any(ToolsEntity.class))).thenReturn(saved);
        doNothing().when(recordservice).saveRecord(any());

        Optional<ToolsEntity> result = toolsService.saveTool(input);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getToolId());
        verify(recordservice, times(1)).saveRecord(any());
        verify(toolsRepository, times(1)).save(any(ToolsEntity.class));
    }
}
