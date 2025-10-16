package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ToolsLoansEntity;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ToolsLoansRepositoryTest {

    @Autowired
    private ToolsLoansRepository toolsLoansRepository;

    private ToolsLoansEntity toolsLoansEntity;

    @BeforeEach
    void setUp() {
        toolsLoansEntity = new ToolsLoansEntity();
        toolsLoansEntity.setLoanId(1L);
        toolsLoansEntity.setToolId(100L);
    }

    @Test
    void testSaveToolsLoansEntity() {
        ToolsLoansEntity saved = toolsLoansRepository.save(toolsLoansEntity);
        assertNotNull(saved.getId());
        assertEquals(1L, saved.getLoanId());
        assertEquals(100L, saved.getToolId());
    }

    @Test
    void testFindById() {
        ToolsLoansEntity saved = toolsLoansRepository.save(toolsLoansEntity);
        ToolsLoansEntity found = toolsLoansRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void testFindAll() {
        toolsLoansRepository.save(toolsLoansEntity);
        List<ToolsLoansEntity> all = toolsLoansRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testFindByLoanId() {
        toolsLoansRepository.save(toolsLoansEntity);
        List<Long> loanTools = toolsLoansRepository.findByLoanId(1L);
        assertNotNull(loanTools);
        assertEquals(1, loanTools.size());
        assertEquals(100L, loanTools.get(0));
    }

    @Test
    void testFindByToolId() {
        toolsLoansRepository.save(toolsLoansEntity);
        List<Long> toolLoans = toolsLoansRepository.findByToolId(100L);
        assertNotNull(toolLoans);
        assertEquals(1, toolLoans.size());
        assertEquals(1L, toolLoans.get(0));
    }

    @Test
    void testDeleteByToolId() {
        ToolsLoansEntity saved = toolsLoansRepository.save(toolsLoansEntity);
        toolsLoansRepository.deleteByToolId(saved.getToolId());
        List<Long> toolLoans = toolsLoansRepository.findByToolId(saved.getToolId());
        assertTrue(toolLoans.isEmpty());
    }
}
