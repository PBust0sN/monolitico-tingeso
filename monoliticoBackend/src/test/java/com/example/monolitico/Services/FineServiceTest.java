package com.example.monolitico.Services;

import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Repositories.FineRepository;
import com.example.monolitico.Service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @InjectMocks
    private FineService fineService;

    private FineEntity fine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fine = new FineEntity();
        fine.setFineId(1L);
        fine.setAmount(5000L);
        fine.setType("fine");
        fine.setClientId(10L);
        fine.setLoanId(20L);
        fine.setState("Pendiente");
        fine.setDate(new Date(System.currentTimeMillis()));
    }

    @Test
    void testGetFineById() {
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));

        FineEntity result = fineService.getFineById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFineId());
        verify(fineRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllFine() {
        when(fineRepository.findAll()).thenReturn(Arrays.asList(fine));

        List<FineEntity> result = fineService.getAllFine();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(fineRepository, times(1)).findAll();
    }

    @Test
    void testSaveFine() {
        when(fineRepository.save(any(FineEntity.class))).thenReturn(fine);

        FineEntity result = fineService.saveFine(fine);

        assertNotNull(result);
        assertEquals(fine.getFineId(), result.getFineId());
        verify(fineRepository, times(1)).save(fine);
    }

    @Test
    void testUpdateFine() {
        when(fineRepository.save(any(FineEntity.class))).thenReturn(fine);

        FineEntity result = fineService.updateFine(fine);

        assertNotNull(result);
        assertEquals(fine.getFineId(), result.getFineId());
        verify(fineRepository, times(1)).save(fine);
    }

    @Test
    void testDeleteFineById_Success() throws Exception {
        doNothing().when(fineRepository).deleteById(1L);

        boolean result = fineService.deleteFineById(1L);

        assertTrue(result);
        verify(fineRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteFineById_Exception() {
        doThrow(new RuntimeException("DB error")).when(fineRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> fineService.deleteFineById(1L));
        assertTrue(exception.getMessage().contains("DB error"));
        verify(fineRepository, times(1)).deleteById(1L);
    }

    @Test
    void testHasFinesByClientId_True() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "fine")).thenReturn(Arrays.asList(fine));

        assertTrue(fineService.hasFinesByClientId(10L));
        verify(fineRepository, times(1)).getFineEntityByClientIdAndTypeIs(10L, "fine");
    }

    @Test
    void testHasFinesByClientId_False() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "fine")).thenReturn(Collections.emptyList());

        assertFalse(fineService.hasFinesByClientId(10L));
        verify(fineRepository, times(1)).getFineEntityByClientIdAndTypeIs(10L, "fine");
    }

    @Test
    void testHasFinesOfToolReposition_True() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "reposition")).thenReturn(Arrays.asList(fine));

        assertTrue(fineService.hasFinesOfToolReposition(10L));
        verify(fineRepository, times(1)).getFineEntityByClientIdAndTypeIs(10L, "reposition");
    }

    @Test
    void testHasFinesOfToolReposition_False() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "reposition")).thenReturn(Collections.emptyList());

        assertFalse(fineService.hasFinesOfToolReposition(10L));
        verify(fineRepository, times(1)).getFineEntityByClientIdAndTypeIs(10L, "reposition");
    }

    @Test
    void testHasLessOrEqual5FinesByClientId_True() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "fine")).thenReturn(Arrays.asList(fine));

        assertTrue(fineService.hasLessOrEqual5FinesByClientId(10L));
        verify(fineRepository, times(1)).getFineEntityByClientIdAndTypeIs(10L, "fine");
    }

    @Test
    void testHasLessOrEqual5FinesByClientId_False() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "fine")).thenReturn(Arrays.asList(fine, fine, fine, fine, fine, fine));

        assertFalse(fineService.hasLessOrEqual5FinesByClientId(10L));
        verify(fineRepository, times(1)).getFineEntityByClientIdAndTypeIs(10L, "fine");
    }
}
