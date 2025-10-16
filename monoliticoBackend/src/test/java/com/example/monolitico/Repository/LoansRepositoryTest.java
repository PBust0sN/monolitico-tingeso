package com.example.monolitico.Repository;

import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Repositories.LoansRepository;
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
class LoansRepositoryTest {

    @Autowired
    private LoansRepository loansRepository;

    private LoansEntity loan;

    @BeforeEach
    void setUp() {
        loan = new LoansEntity();
        loan.setDeliveryDate(Date.valueOf(LocalDate.now().minusDays(2)));
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
        loan.setLoanType("loan");
        loan.setDate(Date.valueOf(LocalDate.now()));
        loan.setStaffId(10L);
        loan.setClientId(20L);
        loan.setAmount(500L);
        loan.setExtraCharges(50L);
        loan.setActive(true);
    }

    @Test
    void testSaveLoan() {
        LoansEntity saved = loansRepository.save(loan);
        assertNotNull(saved.getLoanId());
        assertEquals(loan.getClientId(), saved.getClientId());
    }

    @Test
    void testFindById() {
        LoansEntity saved = loansRepository.save(loan);
        Optional<LoansEntity> found = loansRepository.findById(saved.getLoanId());
        assertTrue(found.isPresent());
        assertEquals(saved.getLoanId(), found.get().getLoanId());
    }

    @Test
    void testFindAll() {
        loansRepository.save(loan);
        List<LoansEntity> all = loansRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteLoan() {
        LoansEntity saved = loansRepository.save(loan);
        loansRepository.deleteById(saved.getLoanId());
        Optional<LoansEntity> deleted = loansRepository.findById(saved.getLoanId());
        assertFalse(deleted.isPresent());
    }
}
