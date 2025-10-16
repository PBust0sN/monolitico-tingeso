package com.example.monolitico.Repository;

import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Repositories.RecordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RecordsRepositoryTest {

    @Autowired
    private RecordsRepository recordsRepository;

    private RecordsEntity record;

    @BeforeEach
    void setUp() {
        record = new RecordsEntity();
        record.setRecordDate(new Date());
        record.setRecordType("Loan");
        record.setRecordAmount(100L);
        record.setClientId(1L);
        record.setLoanId(1L);
        record.setToolId(1L);
    }

    @Test
    void testSaveRecord() {
        RecordsEntity saved = recordsRepository.save(record);
        assertNotNull(saved.getRecordId());
        assertEquals("Loan", saved.getRecordType());
    }

    @Test
    void testFindById() {
        RecordsEntity saved = recordsRepository.save(record);
        Optional<RecordsEntity> found = recordsRepository.findById(saved.getRecordId());
        assertTrue(found.isPresent());
        assertEquals(saved.getRecordId(), found.get().getRecordId());
    }

    @Test
    void testFindAll() {
        recordsRepository.save(record);
        List<RecordsEntity> all = recordsRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteById() {
        RecordsEntity saved = recordsRepository.save(record);
        recordsRepository.deleteById(saved.getRecordId());
        Optional<RecordsEntity> deleted = recordsRepository.findById(saved.getRecordId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByRecordDateBetween() {
        RecordsEntity saved = recordsRepository.save(record);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date start = cal.getTime();
        cal.add(Calendar.DATE, 2);
        Date end = cal.getTime();

        List<RecordsEntity> result = recordsRepository.findByRecordDateBetween(start, end);
        assertFalse(result.isEmpty());
        assertEquals(saved.getRecordId(), result.get(0).getRecordId());
    }

    @Test
    void testFindByRecordType() {
        RecordsEntity saved = recordsRepository.save(record);
        List<RecordsEntity> result = recordsRepository.findByRecordType("Loan");
        assertFalse(result.isEmpty());
        assertEquals(saved.getRecordId(), result.get(0).getRecordId());
    }
}
