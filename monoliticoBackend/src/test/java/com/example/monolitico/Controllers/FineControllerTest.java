package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.FineController;
import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Service.FineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FineControllerTest {

    @Mock
    private FineService fineService;

    @InjectMocks
    private FineController fineController;

    private FineEntity fineExample;

    @BeforeEach
    void setUp() {
        fineExample = new FineEntity(
                1L,
                5000L,
                "Late Payment",
                2L,
                3L,
                "Pending",
                new Date(System.currentTimeMillis())
        );
    }

    @Test
    void testGetFineById() {
        when(fineService.getFineById(1L)).thenReturn(fineExample);

        ResponseEntity<FineEntity> response = fineController.getFineById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Late Payment", response.getBody().getType());
        verify(fineService, times(1)).getFineById(1L);
    }

    @Test
    void testGetAllFine() {
        when(fineService.getAllFine()).thenReturn(Arrays.asList(fineExample));

        ResponseEntity<List<FineEntity>> response = fineController.getAllFine();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(fineService, times(1)).getAllFine();
    }

    @Test
    void testCreateFine() {
        when(fineService.saveFine(any(FineEntity.class))).thenReturn(fineExample);

        ResponseEntity<FineEntity> response = fineController.createFine(fineExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pending", response.getBody().getState());
        verify(fineService, times(1)).saveFine(fineExample);
    }

    @Test
    void testUpdateFine() {
        when(fineService.updateFine(any(FineEntity.class))).thenReturn(fineExample);

        ResponseEntity<FineEntity> response = fineController.updateFine(fineExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fineExample, response.getBody());
        verify(fineService, times(1)).updateFine(fineExample);
    }

    @Test
    void testDeleteFine() throws Exception {
        doReturn(true).when(fineService).deleteFineById(1L);

        ResponseEntity<FineEntity> response = fineController.deleteFine(1L);

        assertEquals(204, response.getStatusCodeValue()); // No Content
        verify(fineService, times(1)).deleteFineById(1L);
    }

    @Test
    void testGetAllClientsById() {
        when(fineService.getAllFinesByClientId(2L)).thenReturn(List.of(fineExample));

        ResponseEntity<List<FineEntity>> response = fineController.getAllClientsById(2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Late Payment", response.getBody().get(0).getType());
        verify(fineService, times(1)).getAllFinesByClientId(2L);
    }

    @Test
    void testPayFine() {
        doNothing().when(fineService).payFine(2L, 1L);

        ResponseEntity<Void> response = fineController.payFine(2L, 1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(fineService, times(1)).payFine(2L, 1L);
    }

}
