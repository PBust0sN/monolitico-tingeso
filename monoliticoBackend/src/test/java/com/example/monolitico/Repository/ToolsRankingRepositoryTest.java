package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Repositories.ToolsRankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ToolsRankingRepositoryTest {

    @Autowired
    private ToolsRankingRepository toolsRankingRepository;

    private ToolsRankingEntity toolsRankingEntity;

    @BeforeEach
    void setUp() {
        toolsRankingEntity = new ToolsRankingEntity();
        toolsRankingEntity.setReportId(10L);
        toolsRankingEntity.setToolId(100L);
    }

    @Test
    void testSaveToolsRankingEntity() {
        ToolsRankingEntity saved = toolsRankingRepository.save(toolsRankingEntity);
        assertNotNull(saved.getToolRankingId());
        assertEquals(10L, saved.getReportId());
        assertEquals(100L, saved.getToolId());
    }

    @Test
    void testFindById() {
        ToolsRankingEntity saved = toolsRankingRepository.save(toolsRankingEntity);
        ToolsRankingEntity found = toolsRankingRepository.findById(saved.getToolRankingId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getToolRankingId(), found.getToolRankingId());
    }

    @Test
    void testFindAll() {
        toolsRankingRepository.save(toolsRankingEntity);
        List<ToolsRankingEntity> all = toolsRankingRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testFindByReportId() {
        toolsRankingRepository.save(toolsRankingEntity);
        List<ToolsRankingEntity> byReport = toolsRankingRepository.findByReportId(10L);
        assertNotNull(byReport);
        assertEquals(1, byReport.size());
        assertEquals(100L, byReport.get(0).getToolId());
    }

    @Test
    void testDelete() {
        ToolsRankingEntity saved = toolsRankingRepository.save(toolsRankingEntity);
        toolsRankingRepository.delete(saved);
        assertTrue(toolsRankingRepository.findAll().isEmpty());
    }
}
