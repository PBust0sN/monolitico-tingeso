package com.example.monolitico.Repository;

import com.example.monolitico.Entities.FineReportEntity;
import com.example.monolitico.Repositories.FineReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FineReportRepositoryTest {

    @Autowired
    private FineReportRepository fineReportRepository;

    private FineReportEntity fineReport;

    @BeforeEach
    void setUp() {
        fineReport = new FineReportEntity();
        fineReport.setReportId(100L);
        fineReport.setAmount(500L);
        fineReport.setType("dmg fine");
        fineReport.setClientId(1L);
        fineReport.setLoanId(10L);
        fineReport.setState("pendiente");
        fineReport.setDate(Date.valueOf("2025-10-15"));
    }

    @Test
    void testSaveFineReport() {
        FineReportEntity saved = fineReportRepository.save(fineReport);
        assertNotNull(saved.getFineReportId());
        assertEquals(fineReport.getAmount(), saved.getAmount());
    }

    @Test
    void testFindById() {
        FineReportEntity saved = fineReportRepository.save(fineReport);
        Optional<FineReportEntity> found = fineReportRepository.findById(saved.getFineReportId());
        assertTrue(found.isPresent());
        assertEquals(saved.getFineReportId(), found.get().getFineReportId());
    }

    @Test
    void testFindAll() {
        fineReportRepository.save(fineReport);
        List<FineReportEntity> all = fineReportRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testFindByReportId() {
        fineReportRepository.save(fineReport);
        List<FineReportEntity> found = fineReportRepository.findByReportId(100L);
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(100L, found.get(0).getReportId());
    }

    @Test
    void testDeleteFineReport() {
        FineReportEntity saved = fineReportRepository.save(fineReport);
        fineReportRepository.deleteById(saved.getFineReportId());
        Optional<FineReportEntity> deleted = fineReportRepository.findById(saved.getFineReportId());
        assertFalse(deleted.isPresent());
    }
}
