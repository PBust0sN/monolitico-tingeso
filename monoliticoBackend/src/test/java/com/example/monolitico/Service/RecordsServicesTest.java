package com.example.monolitico.Service;

import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Repositories.RecordsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecordsServicesTest {

    @Mock
    private RecordsRepository recordsRepository;

    @InjectMocks
    private RecordsServices recordsServices;

    @Test
    public void findByRecordDatesBetween_returns_matching_records() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);
        Date startDate = Date.valueOf(start);
        Date endDate = Date.valueOf(end);

        RecordsEntity r1 = new RecordsEntity();
        r1.setRecordId(1L);
        r1.setRecordDate(Date.valueOf(LocalDate.of(2025,1,10)));

        RecordsEntity r2 = new RecordsEntity();
        r2.setRecordId(2L);
        r2.setRecordDate(Date.valueOf(LocalDate.of(2025,1,20)));

        List<RecordsEntity> list = Arrays.asList(r1, r2);

        when(recordsRepository.findByRecordDateBetween(startDate, endDate)).thenReturn(list);

        List<RecordsEntity> result = recordsServices.findByRecordDatesBetween(startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(recordsRepository, times(1)).findByRecordDateBetween(startDate, endDate);
    }
}
