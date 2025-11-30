package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.RecordsController;
import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Service.RecordsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordsControllerTest {

    @Mock
    private RecordsServices recordsServices;

    @InjectMocks
    private RecordsController recordsController;

    private RecordsEntity recordExample;

    @BeforeEach
    void setUp() {
        recordExample = new RecordsEntity(
                1L, // recordId
                new Date(), // recordDate
                "Ingreso", // recordType
                5000L, // recordAmount
                101L, // clientId
                201L, // loanId
                301L  // toolId
        );
    }

    @Test
    void testGetAllRecords() {
        when(recordsServices.getAllRecords()).thenReturn(List.of(recordExample));

        ResponseEntity<List<RecordsEntity>> response = recordsController.getAllRecords();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(recordExample, response.getBody().get(0));
        verify(recordsServices, times(1)).getAllRecords();
    }

    @Test
    void testGetRecordById() {
        when(recordsServices.getRecordsById(1L)).thenReturn(recordExample);

        ResponseEntity<RecordsEntity> response = recordsController.getRecordById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(recordExample, response.getBody());
        verify(recordsServices, times(1)).getRecordsById(1L);
    }

    @Test
    void testSaveRecord() {
        when(recordsServices.saveRecord(recordExample)).thenReturn(recordExample);

        ResponseEntity<RecordsEntity> response = recordsController.saveRecord(recordExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(recordExample, response.getBody());
        verify(recordsServices, times(1)).saveRecord(recordExample);
    }

    @Test
    void testUpdateRecord() {
        when(recordsServices.updateRecord(recordExample)).thenReturn(recordExample);

        ResponseEntity<RecordsEntity> response = recordsController.updateRecord(recordExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(recordExample, response.getBody());
        verify(recordsServices, times(1)).updateRecord(recordExample);
    }

    @Test
    void testGetRecordsByDate() {
        // Creamos fechas con java.util.Date
        java.util.Date startUtilDate = new java.util.Date(System.currentTimeMillis() - 86400000); // ayer
        java.util.Date endUtilDate = new java.util.Date(System.currentTimeMillis()); // hoy

        // Convertimos a java.sql.Date para el servicio
        java.sql.Date startSqlDate = new java.sql.Date(startUtilDate.getTime());
        java.sql.Date endSqlDate = new java.sql.Date(endUtilDate.getTime());

        when(recordsServices.findByRecordDatesBetween(startSqlDate, endSqlDate))
                .thenReturn(List.of(recordExample));

        ResponseEntity<List<RecordsEntity>> response = recordsController.getRecordsByDate(startSqlDate, endSqlDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(recordExample, response.getBody().get(0));
        verify(recordsServices, times(1)).findByRecordDatesBetween(startSqlDate, endSqlDate);
    }

    @Test
    void testDeleteRecord() throws Exception {
        when(recordsServices.deleteRecord(1L)).thenReturn(true);

        ResponseEntity<RecordsEntity> response = recordsController.deleteRecord(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(recordsServices, times(1)).deleteRecord(1L);
    }
}
