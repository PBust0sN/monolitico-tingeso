package com.example.monolitico.Controllers;

import com.example.monolitico.Controller.ReportsController;
import com.example.monolitico.Entities.ReportsEntity;
import com.example.monolitico.Service.ReportsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportsControllerTest {

    @Mock
    private ReportsServices reportsServices;

    @InjectMocks
    private ReportsController reportsController;

    private ReportsEntity reportExample;

    @BeforeEach
    void setUp() {
        reportExample = new ReportsEntity(
                1L, // reportId
                new Date(System.currentTimeMillis()), // reportDate
                true,  // loanIdReport
                false, // toolsIdRanking
                true,  // fineIdReports
                false, // clientIdBehind
                101L   // clientIdReport
        );
    }

    @Test
    void testGetAllReports() {
        when(reportsServices.getAllReports()).thenReturn(List.of(reportExample));

        ResponseEntity<List<ReportsEntity>> response = reportsController.getAllReports();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(reportExample, response.getBody().get(0));
        verify(reportsServices, times(1)).getAllReports();
    }

    @Test
    void testGetReportById() {
        when(reportsServices.getReportsById(1L)).thenReturn(reportExample);

        ResponseEntity<ReportsEntity> response = reportsController.getRecordById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reportExample, response.getBody());
        verify(reportsServices, times(1)).getReportsById(1L);
    }

    @Test
    void testSaveReport() {
        when(reportsServices.saveReport(reportExample)).thenReturn(reportExample);

        ResponseEntity<ReportsEntity> response = reportsController.saveRecord(reportExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reportExample, response.getBody());
        verify(reportsServices, times(1)).saveReport(reportExample);
    }

    @Test
    void testUpdateReport() {
        when(reportsServices.updateReport(reportExample)).thenReturn(reportExample);

        ResponseEntity<ReportsEntity> response = reportsController.updateRecord(reportExample);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reportExample, response.getBody());
        verify(reportsServices, times(1)).updateReport(reportExample);
    }

    @Test
    void testDeleteReport() throws Exception {
        when(reportsServices.deleteReport(1L)).thenReturn(true);

        ResponseEntity<ReportsEntity> response = reportsController.deleteRecord(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(reportsServices, times(1)).deleteReport(1L);
    }

    @Test
    void testGetAllReports_EmptyList() {
        when(reportsServices.getAllReports()).thenReturn(List.of());

        ResponseEntity<List<ReportsEntity>> response = reportsController.getAllReports();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(reportsServices, times(1)).getAllReports();
    }

    @Test
    void testGetReportById_NotFound() {
        when(reportsServices.getReportsById(99L)).thenReturn(null);

        ResponseEntity<ReportsEntity> response = reportsController.getRecordById(99L);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody()); // El controller igual retorna 200
        verify(reportsServices).getReportsById(99L);
    }

    @Test
    void testGetAllClient() {
        when(reportsServices.getAllByClientId(101L)).thenReturn(List.of(reportExample));

        ResponseEntity<List<ReportsEntity>> response = reportsController.getAllClient(101L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(reportExample, response.getBody().get(0));
        verify(reportsServices).getAllByClientId(101L);
    }

    @Test
    void testGetAllClient_Empty() {
        when(reportsServices.getAllByClientId(101L)).thenReturn(List.of());

        ResponseEntity<List<ReportsEntity>> response = reportsController.getAllClient(101L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(reportsServices).getAllByClientId(101L);
    }

    @Test
    void testSaveReport_NullBody() {
        when(reportsServices.saveReport(null)).thenReturn(null);

        ResponseEntity<ReportsEntity> response = reportsController.saveRecord(null);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(reportsServices).saveReport(null);
    }

    @Test
    void testUpdateReport_NullBody() {
        when(reportsServices.updateReport(null)).thenReturn(null);

        ResponseEntity<ReportsEntity> response = reportsController.updateRecord(null);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(reportsServices).updateReport(null);
    }

    @Test
    void testDeleteReport_NotDeleted() throws Exception {
        when(reportsServices.deleteReport(1L)).thenReturn(false);

        ResponseEntity<ReportsEntity> response = reportsController.deleteRecord(1L);

        assertEquals(204, response.getStatusCodeValue()); // el controller devuelve 204 siempre
        verify(reportsServices).deleteReport(1L);
    }

}
