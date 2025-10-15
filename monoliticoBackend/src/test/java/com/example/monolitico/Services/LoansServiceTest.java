package com.example.monolitico.Services;

import com.example.monolitico.DTO.ReturnLoanDTO;
import com.example.monolitico.Entities.*;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import com.example.monolitico.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LoansServiceTest {

    @Mock
    private LoansRepository loansRepository;

    @Mock
    private ToolsLoansRepository toolsLoansRepository;

    @Mock
    private ToolsLoansService toolsLoansService;

    @Mock
    private ClientService clientService;

    @Mock
    private FineService fineService;

    @Mock
    private ToolsService toolsService;

    @Mock
    private RecordsServices recordsServices;

    @InjectMocks
    private LoansService loansService;

    private LoansEntity loan;
    private ClientEntity client;

    @BeforeEach
    void setUp() {
        loan = new LoansEntity();
        loan.setLoanId(1L);
        loan.setDate(Date.valueOf(LocalDate.now()));
        loan.setDeliveryDate(Date.valueOf(LocalDate.now()));
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
        loan.setStaffId(1L);
        loan.setClientId(1L);
        loan.setAmount(100L);
        loan.setExtraCharges(0L);
        loan.setActive(true);
        loan.setLoanType("loan");

        client = new ClientEntity();
        client.setClient_id(1L);
        client.setAvaliable(true);
        client.setState("activo");
    }

    // =========================
    // saveLoan()
    // =========================
    @Test
    void testSaveLoanWithReturnDate() {
        when(loansRepository.save(loan)).thenReturn(loan);

        Optional<LoansEntity> saved = loansService.saveLoan(loan);

        assertTrue(saved.isPresent());
        assertEquals(loan, saved.get());
        verify(loansRepository, times(1)).save(loan);
    }

    @Test
    void testSaveLoanWithoutReturnDate() {
        loan.setReturnDate(null);
        Optional<LoansEntity> saved = loansService.saveLoan(loan);

        assertTrue(saved.isEmpty());
        verify(loansRepository, never()).save(loan);
    }

    // =========================
    // checkDates()
    // =========================
    @Test
    void testCheckDatesValid() {
        assertTrue(loansService.checkDates(loan));
    }

    @Test
    void testCheckDatesInvalid() {
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(1)));
        assertFalse(loansService.checkDates(loan));
    }

    // =========================
    // findLoanById()
    // =========================
    @Test
    void testFindLoanById() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));

        LoansEntity found = loansService.findLoanById(1L);

        assertEquals(loan, found);
        verify(loansRepository, times(1)).findById(1L);
    }

    // =========================
    // updateLoan()
    // =========================
    @Test
    void testUpdateLoan() {
        when(loansRepository.save(loan)).thenReturn(loan);

        LoansEntity updated = loansService.updateLoan(loan);

        assertEquals(loan, updated);
        verify(loansRepository, times(1)).save(loan);
    }

    // =========================
    // deleteLoan()
    // =========================
    @Test
    void testDeleteLoanSuccess() throws Exception {
        doNothing().when(loansRepository).deleteById(1L);
        when(toolsLoansService.getToolsIDsByLoanId(1L)).thenReturn(List.of(10L, 20L));
        doNothing().when(toolsLoansRepository).deleteByToolId(anyLong());

        boolean result = loansService.deleteLoan(1L);

        assertTrue(result);
        verify(loansRepository, times(1)).deleteById(1L);
        verify(toolsLoansService, times(1)).getToolsIDsByLoanId(1L);
        verify(toolsLoansRepository, times(2)).deleteByToolId(anyLong());
    }

    // =========================
    // calculateCosts()
    // =========================
    @Test
    void testCalculateCostsNoFines() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(Collections.emptyList());

        ReturnLoanDTO dto = loansService.calculateCosts(1L);

        assertNotNull(dto);

        // Repo amount debería ser 0 si no hay daños
        assertEquals(0L, dto.getRepoAmount());

        // Fine amount debería ser 0 si no hay atrasos
        assertEquals(0L, dto.getFineAmount());

        // RepoFine y Fine pueden ser null
        assertNull(dto.getRepoFine());
        assertNull(dto.getFine());
    }


    // =========================
    // calculateRepoFine()
    // =========================
    @Test
    void testCalculateRepoFineNoDamage() {
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(Collections.emptyList());

        ReturnLoanDTO dto = loansService.calculateRepoFine(1L, 1L);

        assertEquals(0L, dto.getRepoAmount());
        assertNull(dto.getRepoFine());
    }

    // =========================
    // calculateFine()
    // =========================
    @Test
    void testCalculateFineNoFine() {
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(Collections.emptyList());
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));

        ReturnLoanDTO dto = loansService.calculateFine(1L, 1L);

        assertEquals(0L, dto.getFineAmount());
        assertNull(dto.getFine());
    }

    // =========================
    // reamaningDaysOnLoan()
    // =========================
    @Test
    void testReamaningDaysOnLoan() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));

        long days = loansService.reamaningDaysOnLoan(1L);

        assertTrue(days > 0);
    }

    // =========================
    // returnLoan()
    // =========================
    @Test
    void testReturnLoan() {
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(Collections.emptyList());
        when(loansRepository.save(any(LoansEntity.class))).thenReturn(loan);

        ReturnLoanDTO dto = loansService.returnLoan(loan);

        assertNotNull(dto);
        assertEquals(loan, dto.getLoan());
        assertFalse(loan.getActive());
    }

    // =========================
    // addLoan() basic scenario
    // =========================
    @Test
    void testAddLoanSuccess() {
        // Mock del cliente
        ClientEntity client = new ClientEntity();
        client.setClient_id(1L);
        client.setAvaliable(true);
        client.setState("activo");

        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(false);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(false);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(false);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(Collections.emptyList());

        // Mock de la herramienta
        ToolsEntity tool = new ToolsEntity();
        tool.setToolId(1L);
        tool.setToolName("Hammer");
        tool.setStock(10L);
        tool.setDisponibility("disponible");
        tool.setLoanFee(50L);

        when(toolsService.getToolsById(1L)).thenReturn(tool);
        when(toolsLoansService.getLoansIDsByToolId(1L)).thenReturn(Collections.emptyList());

        // Mock del repositorio para saveLoan
        when(loansRepository.save(any(LoansEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(toolsService.updateTool(any(ToolsEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(toolsLoansService.saveToolsLoans(any(ToolsLoansEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutar el método
        List<String> errors = loansService.addLoan(1L, 1L, List.of(1L), 5L);

        // Verificaciones
        assertTrue(errors.isEmpty(), "La lista de errores debe estar vacía");
        verify(loansRepository, times(1)).save(any(LoansEntity.class));
        verify(toolsService, times(1)).updateTool(any(ToolsEntity.class));
        verify(toolsLoansService, times(1)).saveToolsLoans(any(ToolsLoansEntity.class));
    }

}
