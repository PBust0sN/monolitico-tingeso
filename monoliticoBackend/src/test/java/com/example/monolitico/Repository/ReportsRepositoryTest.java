package com.example.monolitico.Repository;

import com.example.monolitico.Entities.ReportsEntity;
import com.example.monolitico.Repositories.ReportsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ReportsRepositoryTest {

    @Autowired
    private ReportsRepository reportsRepository;

    private ReportsEntity report;

    @BeforeEach
    void setUp() {
        report = new ReportsEntity();
        report.setReportDate(new Date(System.currentTimeMillis()));
        report.setLoanIdReport(true);
        report.setToolsIdRanking(true);
        report.setFineIdReports(true);
        report.setClientIdBehind(false);
        report.setClientIdReport(1L);
    }

    @Test
    void testSaveReport() {
        ReportsEntity saved = reportsRepository.save(report);
        assertNotNull(saved.getReportId());
        assertTrue(saved.getLoanIdReport());
    }

    @Test
    void testFindById() {
        ReportsEntity saved = reportsRepository.save(report);
        Optional<ReportsEntity> found = reportsRepository.findById(saved.getReportId());
        assertTrue(found.isPresent());
        assertEquals(saved.getReportId(), found.get().getReportId());
    }

    @Test
    void testFindAll() {
        reportsRepository.save(report);
        List<ReportsEntity> all = (List<ReportsEntity>) reportsRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteById() {
        ReportsEntity saved = reportsRepository.save(report);
        reportsRepository.deleteById(saved.getReportId());
        Optional<ReportsEntity> deleted = reportsRepository.findById(saved.getReportId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByReportDateBetween() {
        ReportsEntity saved = reportsRepository.save(report);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.DATE, -1);
        Date start = new Date(cal.getTimeInMillis());
        cal.add(Calendar.DATE, 2);
        Date end = new Date(cal.getTimeInMillis());

        List<ReportsEntity> result = reportsRepository.findByReportDateBetween(start, end);
        assertFalse(result.isEmpty());
        assertEquals(saved.getReportId(), result.get(0).getReportId());
    }
}
