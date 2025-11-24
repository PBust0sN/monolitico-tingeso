package com.example.monolitico.Services;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Repositories.ClientLoansRepository;
import com.example.monolitico.Repositories.ClientRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import com.example.monolitico.Service.ClientService;
import com.example.monolitico.Service.KeycloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientLoansRepository clientLoansRepository;

    @Mock
    private ToolsLoansRepository toolsLoansRepository;

    @Mock
    private KeycloakService keycloakService;

    @InjectMocks
    private ClientService clientService;

    private ClientEntity client;
    private LoansEntity loan;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new ClientEntity(1L, "12345678-9", "John", "Doe", "john@example.com",
                "555-1234", "active", true, "password123");

        loan = new LoansEntity();
        loan.setLoanId(1L);
        loan.setClientId(1L);
        loan.setDeliveryDate(Date.valueOf(LocalDate.now().minusDays(5)));
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
    }

    @Test
    void testSaveClient() {
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(client);
        doNothing().when(keycloakService).createUserInKeycloak(anyString(), anyString(), anyString(), anyLong());

        ClientEntity result = clientService.saveClient(client);

        assertNotNull(result);
        assertEquals(client.getClient_id(), result.getClient_id());
        verify(clientRepository, times(1)).save(client);
        verify(keycloakService, times(1)).createUserInKeycloak(client.getName(), client.getMail(), client.getPassword(), client.getClient_id());
    }

    @Test
    void testGetAllClients() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client));

        List<ClientEntity> result = clientService.getAllClients();

        assertEquals(1, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientEntity result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals(client.getName(), result.getName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateClient() {
        when(clientRepository.save(client)).thenReturn(client);

        ClientEntity result = clientService.updateClient(client);

        assertEquals(client.getClient_id(), result.getClient_id());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testDeleteClient() throws Exception {
        doNothing().when(clientRepository).deleteById(1L);

        boolean result = clientService.deleteClient(1L);

        assertTrue(result);
        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testHasExpiredLoansById_NoLoans() {
        when(clientRepository.getAllLoansByClientId(1L)).thenReturn(Collections.emptyList());

        boolean result = clientService.hasExpiredLoansById(1L);

        assertFalse(result);
        verify(clientRepository, times(1)).getAllLoansByClientId(1L);
    }

    @Test
    void testHasExpiredLoansById_WithLoans() {
        when(clientRepository.getAllLoansByClientId(1L)).thenReturn(Arrays.asList(loan));

        boolean result = clientService.hasExpiredLoansById(1L);

        assertFalse(result); // loan return date > delivery date, so not expired
    }

    @Test
    void testHasTheSameToolInLoanByClientId() {
        when(clientLoansRepository.findLoansIdsByClientId(1L)).thenReturn(Arrays.asList(1L));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(Arrays.asList(100L));

        boolean result = clientService.HasTheSameToolInLoanByClientId(1L, 100L);

        assertTrue(result);
    }

    @Test
    void testFindByRut() {
        when(clientRepository.findByRut("12345678-9")).thenReturn(client);

        ClientEntity result = clientService.findByRut("12345678-9");

        assertNotNull(result);
        assertEquals("John", result.getName());
    }

    @Test
    void testFindAllLoansByClientId() {
        when(clientRepository.getAllLoansByClientId(1L)).thenReturn(Arrays.asList(loan));

        List<LoansEntity> result = clientService.findALlLoansByClientId(1L);

        assertEquals(1, result.size());
        assertEquals(loan.getLoanId(), result.get(0).getLoanId());
    }

    @Test
    void testGetClientById_NotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> clientService.getClientById(99L));

        verify(clientRepository, times(1)).findById(99L);
    }

    @Test
    void testDeleteClient_Exception() {
        doThrow(new RuntimeException("DB error"))
                .when(clientRepository).deleteById(1L);

        assertThrows(Exception.class, () -> clientService.deleteClient(1L));

        verify(clientRepository, times(1)).deleteById(1L);
    }

    @Test
    void testHasExpiredLoansById_WithExpiredLoan() {
        LoansEntity expiredLoan = new LoansEntity();
        expiredLoan.setDeliveryDate(Date.valueOf(LocalDate.now()));
        expiredLoan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(3))); // vencido

        when(clientRepository.getAllLoansByClientId(1L))
                .thenReturn(Arrays.asList(expiredLoan));

        boolean result = clientService.hasExpiredLoansById(1L);

        assertTrue(result);
    }
    @Test
    void testHasTheSameToolInLoanByClientId_NotFound() {
        when(clientLoansRepository.findLoansIdsByClientId(1L)).thenReturn(Arrays.asList(1L));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(Arrays.asList(200L)); // distinta

        boolean result = clientService.HasTheSameToolInLoanByClientId(1L, 100L);

        assertFalse(result);
    }
    @Test
    void testSaveClient_KeycloakThrowsException() {
        when(clientRepository.save(any())).thenReturn(client);
        doThrow(new RuntimeException("Keycloak error"))
                .when(keycloakService)
                .createUserInKeycloak(anyString(), anyString(), anyString(), anyLong());

        assertThrows(RuntimeException.class, () -> clientService.saveClient(client));
    }

}
