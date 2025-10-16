package com.example.monolitico.Repository;

import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Repositories.FineRepository;
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
class FineRepositoryTest {

    @Autowired
    private FineRepository fineRepository;

    private FineEntity fine;

    @BeforeEach
    void setUp() {
        fine = new FineEntity();
        fine.setAmount(1000L);
        fine.setType("dmg fine");
        fine.setClientId(1L);
        fine.setLoanId(10L);
        fine.setState("pendiente");
        fine.setDate(Date.valueOf("2025-10-15"));
    }

    @Test
    void testSaveFine() {
        FineEntity saved = fineRepository.save(fine);
        assertNotNull(saved.getFineId());
        assertEquals(fine.getAmount(), saved.getAmount());
        assertEquals(fine.getType(), saved.getType());
    }

    @Test
    void testFindById() {
        FineEntity saved = fineRepository.save(fine);
        Optional<FineEntity> found = fineRepository.findById(saved.getFineId());
        assertTrue(found.isPresent());
        assertEquals(saved.getFineId(), found.get().getFineId());
    }

    @Test
    void testFindAll() {
        fineRepository.save(fine);
        List<FineEntity> all = fineRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testGetFineEntityByClientIdAndTypeIs() {
        fineRepository.save(fine);
        List<FineEntity> result = fineRepository.getFineEntityByClientIdAndTypeIs(1L, "dmg fine");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("dmg fine", result.get(0).getType());
        assertEquals(1L, result.get(0).getClientId());
    }

    @Test
    void testDeleteFine() {
        FineEntity saved = fineRepository.save(fine);
        fineRepository.deleteById(saved.getFineId());
        Optional<FineEntity> deleted = fineRepository.findById(saved.getFineId());
        assertFalse(deleted.isPresent());
    }
}
