package com.example.monolitico.Services;

import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.ToolsRepository;
import com.example.monolitico.Service.RecordsServices;
import com.example.monolitico.Service.StaffService;
import com.example.monolitico.Service.ToolsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToolsServiceTest {

    @InjectMocks
    private ToolsService toolsService;

    @Mock
    private ToolsRepository toolsRepository;

    @Mock
    private RecordsServices recordsServices;

    @Mock
    private StaffService staffService;

    private ToolsEntity tool;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tool = new ToolsEntity();
        tool.setToolId(1L);
        tool.setToolName("Hammer");
        tool.setCategory("Hand Tools");
        tool.setRepositionFee(100L);
        tool.setInitialState("Bueno");
        tool.setDisponibility("Disponible");
        tool.setStock(10L);
    }

    @Test
    void testGetAllTools() {
        List<ToolsEntity> toolsList = List.of(tool);
        when(toolsRepository.findAll()).thenReturn(toolsList);

        List<ToolsEntity> result = toolsService.getAllTools();

        assertEquals(1, result.size());
        verify(toolsRepository, times(1)).findAll();
    }

    @Test
    void testSaveToolSuccess() {
        when(toolsRepository.save(tool)).thenReturn(tool);
        when(recordsServices.saveRecord(any(RecordsEntity.class))).thenReturn(new RecordsEntity());

        Optional<ToolsEntity> result = toolsService.saveTool(tool);

        assertTrue(result.isPresent());
        assertEquals(tool.getToolId(), result.get().getToolId());
        verify(toolsRepository, times(1)).save(tool);
        verify(recordsServices, times(1)).saveRecord(any(RecordsEntity.class));
    }

    @Test
    void testSaveToolFailsWhenNameNull() {
        tool.setToolName(null);

        Optional<ToolsEntity> result = toolsService.saveTool(tool);

        assertTrue(result.isEmpty());
        verify(toolsRepository, never()).save(tool);
        verify(recordsServices, never()).saveRecord(any(RecordsEntity.class));
    }

    @Test
    void testGetToolsById() {
        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));

        ToolsEntity result = toolsService.getToolsById(1L);

        assertEquals("Hammer", result.getToolName());
        verify(toolsRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTool() {
        when(toolsRepository.save(tool)).thenReturn(tool);

        ToolsEntity result = toolsService.updateTool(tool);

        assertEquals(tool.getToolId(), result.getToolId());
        verify(toolsRepository, times(1)).save(tool);
    }

    @Test
    void testDropDownATool() {
        StaffEntity staff = new StaffEntity();
        staff.setStaffId(1L);
        when(staffService.getStaffById(1L)).thenReturn(staff);
        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(toolsRepository.save(any(ToolsEntity.class))).thenReturn(tool);

        Optional<ToolsEntity> result = toolsService.dropDownATool(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals("dada de baja", result.get().getInitialState());
        verify(toolsRepository, times(1)).save(any(ToolsEntity.class));
    }

    @Test
    void testDeleteTools() throws Exception {
        doNothing().when(toolsRepository).deleteById(1L);

        boolean result = toolsService.deleteTools(1L);

        assertTrue(result);
        verify(toolsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testHasDisponibilityAndStock() {
        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));

        boolean result = toolsService.hasDisponibilityAndStock(1L);

        assertTrue(result);
    }

    @Test
    void testUpdateStateById() {
        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(toolsRepository.save(tool)).thenReturn(tool);

        ToolsEntity result = toolsService.updateStateById(1L, "Malo");

        assertEquals("Malo", result.getInitialState());
    }

    @Test
    void testUpdateStockById() {
        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(toolsRepository.save(tool)).thenReturn(tool);

        ToolsEntity result = toolsService.updateStockById(1L);

        assertEquals(11L, result.getStock());
    }

    @Test
    void testSaveToolFailsWhenCategoryNull() {
        tool.setCategory(null);

        Optional<ToolsEntity> result = toolsService.saveTool(tool);

        assertTrue(result.isEmpty());
        verify(toolsRepository, never()).save(any());
        verify(recordsServices, never()).saveRecord(any());
    }

    @Test
    void testSaveToolFailsWhenRepositionFeeNull() {
        tool.setRepositionFee(null);

        Optional<ToolsEntity> result = toolsService.saveTool(tool);

        assertTrue(result.isEmpty());
        verify(toolsRepository, never()).save(any());
        verify(recordsServices, never()).saveRecord(any());
    }

    @Test
    void testDeleteToolsThrowsException() {
        doThrow(new RuntimeException("DB error"))
                .when(toolsRepository).deleteById(1L);

        Exception e = assertThrows(Exception.class, () -> toolsService.deleteTools(1L));

        assertEquals("DB error", e.getMessage());
    }

    @Test
    void testHasDisponibilityAndStockFalse() {
        tool.setDisponibility("No disponible");
        tool.setStock(0L);

        when(toolsRepository.findById(1L)).thenReturn(Optional.of(tool));

        boolean result = toolsService.hasDisponibilityAndStock(1L);

        assertFalse(result);
    }

    @Test
    void testFindRankingMax10() {
        when(toolsRepository.findRanking()).thenReturn(List.of(tool));

        List<ToolsEntity> result = toolsService.findRankingMax10();

        assertEquals(1, result.size());
        verify(toolsRepository, times(1)).findRanking();
    }

}
