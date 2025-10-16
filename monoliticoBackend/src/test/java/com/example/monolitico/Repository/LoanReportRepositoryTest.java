package com.example.monolitico.Repository;

import com.example.monolitico.Entities.LoansReportEntity;
import com.example.monolitico.Repositories.LoanReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LoanReportRepositoryTest {

    @Autowired
    private LoanReportRepository loanReportRepository;

    private LoansReportEntity report;

    @BeforeEach
    void setUp() {
        report = new LoansReportEntity();
        report.setReportId(1L);
        report.setClientIdBehind(2L);
        report.setDeliveryDate(Date.valueOf(LocalDate.now().minusDays(5)));
        report.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
        report.setLoanType("loan");
        report.setDate(Date.valueOf(LocalDate.now()));
        report.setStaffId(10L);
        report.setClientId(20L);
        report.setAmount(500L);
        report.setExtraCharges(50L);
    }

    @Test
    void testSaveLoanReport() {
        LoansReportEntity saved = loanReportRepository.save(report);
        assertNotNull(saved.getLoanReportId());
        assertEquals(report.getReportId(), saved.getReportId());
    }

    @Test
    void testFindById() {
        LoansReportEntity saved = loanReportRepository.save(report);
        Optional<LoansReportEntity> found = loanReportRepository.findById(saved.getLoanReportId());
        assertTrue(found.isPresent());
        assertEquals(saved.getLoanReportId(), found.get().getLoanReportId());
    }

    @Test
    void testFindAll() {
        loanReportRepository.save(report);
        List<LoansReportEntity> all = loanReportRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testFindByReportId() {
        loanReportRepository.save(report);
        List<LoansReportEntity> result = loanReportRepository.findByReportId(1L);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getReportId());
    }

    @Test
    void testDeleteLoanReport() {
        LoansReportEntity saved = loanReportRepository.save(report);
        loanReportRepository.deleteById(saved.getLoanReportId());
        Optional<LoansReportEntity> deleted = loanReportRepository.findById(saved.getLoanReportId());
        assertFalse(deleted.isPresent());
    }
}
