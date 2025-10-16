package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ToolsReportEntity;
import com.example.monolitico.Repositories.ToolsReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ToolsReportRepositoryTest {

    @Autowired
    private ToolsReportRepository toolsReportRepository;

    private ToolsReportEntity toolReport;

    @BeforeEach
    void setUp() {
        toolReport = new ToolsReportEntity();
        toolReport.setToolName("Taladro");
        toolReport.setCategory("Herramienta");
        toolReport.setLoanCount(5L);
    }

    @Test
    void testSaveToolsReport() {
        ToolsReportEntity saved = toolsReportRepository.save(toolReport);
        assertNotNull(saved.getToolIdReport());
        assertEquals("Taladro", saved.getToolName());
        assertEquals("Herramienta", saved.getCategory());
        assertEquals(5L, saved.getLoanCount());
    }

    @Test
    void testFindById() {
        ToolsReportEntity saved = toolsReportRepository.save(toolReport);
        ToolsReportEntity found = toolsReportRepository.findById(saved.getToolIdReport()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getToolIdReport(), found.getToolIdReport());
    }

    @Test
    void testFindAll() {
        toolsReportRepository.save(toolReport);
        List<ToolsReportEntity> all = toolsReportRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDelete() {
        ToolsReportEntity saved = toolsReportRepository.save(toolReport);
        toolsReportRepository.delete(saved);
        assertTrue(toolsReportRepository.findAll().isEmpty());
    }
}
