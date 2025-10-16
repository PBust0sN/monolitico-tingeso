package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.ToolsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ToolsRepositoryTest {

    @Autowired
    private ToolsRepository toolsRepository;

    private ToolsEntity tool1;
    private ToolsEntity tool2;

    @BeforeEach
    void setUp() {
        tool1 = new ToolsEntity();
        tool1.setToolName("Taladro");
        tool1.setInitialState("Bueno");
        tool1.setDisponibility("Disponible");
        tool1.setCategory("Electrica");
        tool1.setLoanFee(100L);
        tool1.setRepositionFee(500L);
        tool1.setDiaryFineFee(20L);
        tool1.setStock(5L);
        tool1.setLoanCount(10L);
        tool1.setLowDmgFee(50L);

        tool2 = new ToolsEntity();
        tool2.setToolName("Martillo");
        tool2.setInitialState("Bueno");
        tool2.setDisponibility("No disponible");
        tool2.setCategory("Manual");
        tool2.setLoanFee(50L);
        tool2.setRepositionFee(200L);
        tool2.setDiaryFineFee(10L);
        tool2.setStock(2L);
        tool2.setLoanCount(5L);
        tool2.setLowDmgFee(20L);

        toolsRepository.save(tool1);
        toolsRepository.save(tool2);
    }

    @Test
    void testFindByToolName() {
        List<ToolsEntity> result = toolsRepository.findByToolName("Taladro");
        assertEquals(1, result.size());
        assertEquals("Taladro", result.get(0).getToolName());
    }

    @Test
    void testFindByDisponibility() {
        List<ToolsEntity> result = toolsRepository.findByDisponibility("Disponible");
        assertEquals(1, result.size());
        assertEquals("Taladro", result.get(0).getToolName());
    }

    @Test
    void testFindByCategory() {
        List<ToolsEntity> result = toolsRepository.findByCategory("Electrica");
        assertEquals(1, result.size());
        assertEquals("Taladro", result.get(0).getToolName());
    }

    @Test
    void testFindStockByToolName() {
        Long stock = toolsRepository.findStockByToolName("Taladro");
        assertEquals(1L, stock); // porque hay un registro con 'Disponible' y toolName 'Taladro'
    }

    @Test
    void testFindRanking() {
        List<ToolsEntity> ranking = toolsRepository.findRanking();
        assertEquals(2, ranking.size());
        assertEquals("Taladro", ranking.get(0).getToolName()); // Taladro tiene loanCount mayor
        assertEquals("Martillo", ranking.get(1).getToolName());
    }

    @Test
    void testSaveAndDelete() {
        ToolsEntity tool = new ToolsEntity();
        tool.setToolName("Sierra");
        tool.setDisponibility("Disponible");
        toolsRepository.save(tool);

        assertNotNull(tool.getToolId());

        toolsRepository.delete(tool);
        assertTrue(toolsRepository.findById(tool.getToolId()).isEmpty());
    }
}
