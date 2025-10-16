package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ToolsLoanReportEntity;
import com.example.monolitico.Repositories.ToolsLoanReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ToolsLoanReportRepositoryTest {

    @Autowired
    private ToolsLoanReportRepository toolsLoanReportRepository;

    private ToolsLoanReportEntity reportEntity;

    @BeforeEach
    void setUp() {
        reportEntity = new ToolsLoanReportEntity();
        reportEntity.setLoanId(1L);
        reportEntity.setToolId(100L);
    }

    @Test
    void testSaveToolsLoanReport() {
        ToolsLoanReportEntity saved = toolsLoanReportRepository.save(reportEntity);
        assertNotNull(saved.getToolLoanReportId());
        assertEquals(1L, saved.getLoanId());
        assertEquals(100L, saved.getToolId());
    }

    @Test
    void testFindById() {
        ToolsLoanReportEntity saved = toolsLoanReportRepository.save(reportEntity);
        ToolsLoanReportEntity found = toolsLoanReportRepository.findById(saved.getToolLoanReportId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getToolLoanReportId(), found.getToolLoanReportId());
    }

    @Test
    void testFindAll() {
        toolsLoanReportRepository.save(reportEntity);
        List<ToolsLoanReportEntity> all = toolsLoanReportRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteById() {
        ToolsLoanReportEntity saved = toolsLoanReportRepository.save(reportEntity);
        toolsLoanReportRepository.deleteById(saved.getToolLoanReportId());
        assertFalse(toolsLoanReportRepository.findById(saved.getToolLoanReportId()).isPresent());
    }

    @Test
    void testFindToolIdByLoanId() {
        toolsLoanReportRepository.save(reportEntity);
        List<Long> toolIds = toolsLoanReportRepository.findToolIdByLoanId(1L);
        assertNotNull(toolIds);
        assertEquals(1, toolIds.size());
        assertEquals(100L, toolIds.get(0));
    }
}
