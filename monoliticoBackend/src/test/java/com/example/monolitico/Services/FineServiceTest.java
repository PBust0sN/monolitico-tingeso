package com.example.monolitico.Services;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Repositories.FineRepository;
import com.example.monolitico.Service.ClientService;
import com.example.monolitico.Service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private FineService fineService;

    private FineEntity fine;
    private ClientEntity client;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        fine = new FineEntity(1L, 100L, "reposition", 10L, 20L, "pendiente", new Date(System.currentTimeMillis()));

        client = new ClientEntity();
        client.setClient_id(1L);
        client.setState("activo");
    }

    @Test
    void testGetFineById() {
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));

        FineEntity result = fineService.getFineById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFineId());
    }

    @Test
    void testGetAllFine() {
        when(fineRepository.findAll()).thenReturn(List.of(fine));

        List<FineEntity> result = fineService.getAllFine();

        assertEquals(1, result.size());
    }



    @Test
    void testSaveFine() {
        when(fineRepository.save(fine)).thenReturn(fine);

        FineEntity result = fineService.saveFine(fine);

        assertEquals(fine, result);
    }

    @Test
    void testUpdateFine() {
        when(fineRepository.save(fine)).thenReturn(fine);

        FineEntity result = fineService.updateFine(fine);

        assertEquals(fine, result);
    }

    @Test
    void testDeleteFineByIdSuccess() throws Exception {
        doNothing().when(fineRepository).deleteById(1L);

        boolean result = fineService.deleteFineById(1L);

        assertTrue(result);
    }

    @Test
    void testDeleteFineByIdThrowsException() {
        doThrow(new RuntimeException("error")).when(fineRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> {
            fineService.deleteFineById(1L);
        });

        assertEquals("error", exception.getMessage());
    }

    @Test
    void testHasFinesByClientIdTrue() {
        when(fineRepository.findByClientId(10L)).thenReturn(List.of(fine));

        assertTrue(fineService.hasFinesByClientId(10L));
    }

    @Test
    void testHasFinesByClientIdFalse() {
        when(fineRepository.findByClientId(10L)).thenReturn(Collections.emptyList());

        assertFalse(fineService.hasFinesByClientId(10L));
    }

    @Test
    void testHasFinesOfToolRepositionTrue() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "reposition"))
                .thenReturn(List.of(fine));

        assertTrue(fineService.hasFinesOfToolReposition(10L));
    }

    @Test
    void testHasFinesOfToolRepositionFalse() {
        when(fineRepository.getFineEntityByClientIdAndTypeIs(10L, "reposition"))
                .thenReturn(Collections.emptyList());

        assertFalse(fineService.hasFinesOfToolReposition(10L));
    }

    @Test
    void testHasLessOrEqual5FinesTrue() {
        when(fineRepository.findByClientId(10L)).thenReturn(List.of(fine, fine));

        assertTrue(fineService.hasLessOrEqual5FinesByClientId(10L));
    }

    @Test
    void testHasLessOrEqual5FinesFalse() {
        when(fineRepository.findByClientId(10L)).thenReturn(List.of(fine, fine, fine, fine, fine, fine));

        assertFalse(fineService.hasLessOrEqual5FinesByClientId(10L));
    }

    @Test
    void testPayFine() {
        ClientEntity client = new ClientEntity();
        client.setState("bloqueado");

        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.findByClientId(1L)).thenReturn(List.of(fine));

        fineService.payFine(1L, 1L);

        assertEquals("pagado", fine.getState());
        verify(fineRepository, times(1)).save(fine);
    }

    @Test
    void testPayFineActivatesClient() {
        ClientEntity client = new ClientEntity();
        client.setState("bloqueado");

        when(clientService.getClientById(10L)).thenReturn(client);
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.findByClientId(10L)).thenReturn(List.of(fine)); // 1 multa

        fineService.payFine(10L, 1L);

        assertEquals("pagado", fine.getState());
        verify(clientService, times(1)).updateClient(client);
        assertEquals("activo", client.getState());
    }
    @Test
    void testGetFineById_NotFound() {
        when(fineRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> fineService.getFineById(99L));
    }

    @Test
    void testGetAllFinesByClientId() {
        when(fineRepository.findByClientId(10L)).thenReturn(List.of(fine));

        List<FineEntity> result = fineService.getAllFinesByClientId(10L);

        assertEquals(1, result.size());
        verify(fineRepository, times(1)).findByClientId(10L);
    }

    @Test
    void testPayFine_DoesNotActivateClient() {
        ClientEntity client = new ClientEntity();
        client.setState("bloqueado");

        // fineRepository.findByClientId → varias multas pendientes
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.getPendingFinesByClientId(1L))
                .thenReturn(List.of(new FineEntity(), new FineEntity())); // 2 pendientes

        fineService.payFine(1L, 1L);

        assertEquals("pagado", fine.getState());
        verify(fineRepository, times(1)).save(fine);

        // No debería activar al cliente
        verify(clientService, never()).updateClient(any());
    }

    @Test
    void testPayFine_FineNotFound() {
        when(fineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> fineService.payFine(10L, 1L));
    }

    @Test
    void testPayFine_RepositoryPendingFinesCalled() {
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
        when(fineRepository.getPendingFinesByClientId(1L)).thenReturn(Collections.emptyList());

        fineService.payFine(1L, 1L);

        verify(fineRepository, times(1)).getPendingFinesByClientId(1L);
    }

    @Test
    void testSaveFine_Exception() {
        when(fineRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> fineService.saveFine(fine));
    }

    @Test
    void testUpdateFine_Exception() {
        when(fineRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> fineService.updateFine(fine));
    }

}
