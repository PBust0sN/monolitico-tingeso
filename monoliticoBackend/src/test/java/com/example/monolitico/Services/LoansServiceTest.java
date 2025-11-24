package com.example.monolitico.Services;

import com.example.monolitico.DTO.ReturnLoanDTO;
import com.example.monolitico.Entities.*;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import com.example.monolitico.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoansServiceTest {

    @Mock
    private LoansRepository loansRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private FineService fineService;

    @Mock
    private ToolsService toolsService;

    @Mock
    private ToolsLoansRepository toolsLoansRepository;

    @Mock
    private ToolsLoansService toolsLoansService;

    @Mock
    private RecordsServices recordsServices;

    @InjectMocks
    private LoansService loansService;

    private LoansEntity loan;
    private ClientEntity client;
    private ToolsEntity tool;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new ClientEntity();
        client.setClient_id(1L);
        client.setAvaliable(true);
        client.setState("activo");

        loan = new LoansEntity();
        loan.setLoanId(1L);
        loan.setClientId(1L);
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(5)));
        loan.setActive(true);

        tool = new ToolsEntity();
        tool.setToolId(1L);
        tool.setToolName("Martillo");
        tool.setStock(5L);
        tool.setLoanFee(100L);
        tool.setDisponibility("Disponible");
        tool.setInitialState("Bueno");
        tool.setLoanCount(2L);
    }

    @Test
    void testGetAllLoans() {
        when(loansRepository.findAll()).thenReturn(List.of(loan));
        List<LoansEntity> result = loansService.getAllLoans();
        assertEquals(1, result.size());
        verify(loansRepository, times(1)).findAll();
    }

    @Test
    void testSaveLoan_WithReturnDate() {
        when(loansRepository.save(loan)).thenReturn(loan);
        Optional<LoansEntity> result = loansService.saveLoan(loan);
        assertTrue(result.isPresent());
    }

    @Test
    void testSaveLoan_WithoutReturnDate() {
        loan.setReturnDate(null);
        Optional<LoansEntity> result = loansService.saveLoan(loan);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAddLoan_AllChecksPass() {
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(false);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(false);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(false);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(Collections.emptyList());
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        when(toolsLoansService.getLoansIDsByToolId(1L)).thenReturn(Collections.emptyList());
        when(clientService.HasTheSameToolInLoanByClientId(1L, 1L)).thenReturn(false);
        when(loansRepository.save(any())).thenReturn(loan);


        List<String> errors = loansService.addLoan(10L, 1L, List.of(1L), 5L);
        assertTrue(errors.isEmpty());
        verify(toolsService, times(1)).updateTool(any());
        verify(toolsLoansService, times(1)).saveToolsLoans(any());
    }

    @Test
    void testAddLoan_WithErrors() {
        client.setAvaliable(false);
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(true);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(true);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(true);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(List.of(loan, loan, loan, loan, loan));
        List<String> errors = loansService.addLoan(10L, 1L, List.of(1L), 5L);
        assertFalse(errors.isEmpty());
        assertEquals(5, errors.size());
    }

    @Test
    void testCheckDates() {
        loan.setReturnDate(Date.valueOf(LocalDate.now().plusDays(1)));
        assertTrue(loansService.checkDates(loan));
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(1)));
        assertFalse(loansService.checkDates(loan));
    }

    @Test
    void testReturnLoan() {
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(1L));
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loansRepository.save(any())).thenReturn(loan);

        ReturnLoanDTO dto = loansService.returnLoan(loan);
        assertNotNull(dto.getLoan());
        verify(recordsServices, times(1)).saveRecord(any());
    }

    @Test
    void testCalculateCosts_PositiveAndNegativeDays() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(1L));
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        when(fineService.saveFine(any())).thenReturn(new FineEntity());

        ReturnLoanDTO dto = loansService.calculateCosts(1L);
        assertNotNull(dto);
    }

    @Test
    void testRemainingDaysOnLoan() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        assertTrue(loansService.reamaningDaysOnLoan(1L) > 0);
    }

    @Test
    void testFindLoanById() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        LoansEntity result = loansService.findLoanById(1L);
        assertEquals(1L, result.getLoanId());
    }

    @Test
    void testUpdateLoan() {
        when(loansRepository.save(loan)).thenReturn(loan);
        LoansEntity updated = loansService.updateLoan(loan);
        assertEquals(loan, updated);
    }

    @Test
    void testDeleteLoan_Success() throws Exception {
        when(toolsLoansService.getToolsIDsByLoanId(1L)).thenReturn(List.of(1L));
        doNothing().when(loansRepository).deleteById(1L);
        doNothing().when(toolsLoansRepository).deleteByToolId(1L);

        boolean result = loansService.deleteLoan(1L);
        assertTrue(result);
    }

    @Test
    void testDeleteLoan_Exception() {
        doThrow(new RuntimeException("DB error")).when(loansRepository).deleteById(1L);
        Exception ex = assertThrows(Exception.class, () -> loansService.deleteLoan(1L));
        assertTrue(ex.getMessage().contains("DB error"));
    }

    @Test
    void testAddLoan_ToolErrors() {
        client.setAvaliable(true);
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(false);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(false);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(false);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(Collections.emptyList());

        // Tool con stock 0
        ToolsEntity toolZeroStock = new ToolsEntity();
        toolZeroStock.setToolId(2L);
        toolZeroStock.setToolName("Destornillador");
        toolZeroStock.setStock(0L);
        toolZeroStock.setDisponibility("disponible");
        toolZeroStock.setLoanFee(50L);

        when(toolsService.getToolsById(2L)).thenReturn(toolZeroStock);
        when(toolsLoansService.getLoansIDsByToolId(2L)).thenReturn(Collections.emptyList());
        when(clientService.HasTheSameToolInLoanByClientId(1L, 2L)).thenReturn(false);

        List<String> errors = loansService.addLoan(1L, 1L, List.of(2L), 5L);
        assertTrue(errors.contains("Tool Destornillador isnt in stock."));
    }

    @Test
    void testAddLoan_ClientAlreadyHasTool() {
        client.setAvaliable(true);
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(false);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(false);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(false);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(Collections.emptyList());
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        when(toolsLoansService.getLoansIDsByToolId(1L)).thenReturn(Collections.emptyList());
        when(clientService.HasTheSameToolInLoanByClientId(1L, 1L)).thenReturn(true);

        List<String> errors = loansService.addLoan(1L, 1L, List.of(1L), 5L);
        assertTrue(errors.contains("Client already has a loan with tool: Martillo"));
    }

    @Test
    void testReturnLoan_DamagedTool() {
        ToolsEntity damagedTool = new ToolsEntity();
        damagedTool.setToolId(1L);
        damagedTool.setToolName("Taladro");
        damagedTool.setInitialState("Dañada");
        damagedTool.setLowDmgFee(20L);
        damagedTool.setRepositionFee(400L);
        damagedTool.setLoanCount(2L);

        when(clientService.getClientById(1L)).thenReturn(client);
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(1L));
        when(toolsService.getToolsById(1L)).thenReturn(damagedTool);
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loansRepository.save(any())).thenReturn(loan);

        ReturnLoanDTO dto = loansService.returnLoan(loan);
        assertNotNull(dto);
        assertEquals(0, dto.getLowDmgAmount()); // porque "Dañada" no suma low damage
    }

    @Test
    void testCalculateRepoFine_NoDamagedTools() {
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(1L));
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        ReturnLoanDTO dto = loansService.calculateRepoFine(1L, 1L);
        assertEquals(0L, dto.getRepoAmount());
    }

    @Test
    void testCalculateFine_NoLate() {
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(1L));
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        ReturnLoanDTO dto = loansService.calculateFine(1L, 1L);
        assertEquals(0L, dto.getFineAmount());
    }

    @Test
    void testReamaningDaysOnLoan_Late() {
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(3)));
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        long days = loansService.reamaningDaysOnLoan(1L);
        assertTrue(days < 0);
    }

    @Test
    void testDeleteLoan_NoTools() throws Exception {
        when(toolsLoansService.getToolsIDsByLoanId(1L)).thenReturn(Collections.emptyList());
        doNothing().when(loansRepository).deleteById(1L);

        boolean result = loansService.deleteLoan(1L);
        assertTrue(result);
    }@Test
    void testAddLoan_ToolNotDisponible() {
        client.setAvaliable(true);
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(false);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(false);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(false);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(Collections.emptyList());

        ToolsEntity unavailableTool = new ToolsEntity();
        unavailableTool.setToolId(2L);
        unavailableTool.setToolName("Sierra");
        unavailableTool.setStock(5L);
        unavailableTool.setDisponibility("no disponible");
        unavailableTool.setLoanFee(50L);

        when(toolsService.getToolsById(2L)).thenReturn(unavailableTool);
        when(toolsLoansService.getLoansIDsByToolId(2L)).thenReturn(Collections.emptyList());
        when(clientService.HasTheSameToolInLoanByClientId(1L, 2L)).thenReturn(false);

        List<String> errors = loansService.addLoan(1L, 1L, List.of(2L), 5L);
        assertTrue(errors.contains("Tool Sierra hans't disponibility."));
    }

    @Test
    void testAddLoan_TooManyToolsLoaned() {
        client.setAvaliable(true);
        when(clientService.getClientById(1L)).thenReturn(client);
        when(fineService.hasFinesByClientId(1L)).thenReturn(false);
        when(fineService.hasFinesOfToolReposition(1L)).thenReturn(false);
        when(clientService.hasExpiredLoansById(1L)).thenReturn(false);
        when(clientService.findALlLoansByClientId(1L)).thenReturn(Collections.emptyList());

        ToolsEntity limitedTool = new ToolsEntity();
        limitedTool.setToolId(3L);
        limitedTool.setToolName("Taladro");
        limitedTool.setStock(1L);
        limitedTool.setDisponibility("disponible");
        limitedTool.setLoanFee(50L);

        when(toolsService.getToolsById(3L)).thenReturn(limitedTool);
        when(toolsLoansService.getLoansIDsByToolId(3L)).thenReturn(List.of(1L, 2L)); // más que stock
        when(clientService.HasTheSameToolInLoanByClientId(1L, 3L)).thenReturn(false);

        List<String> errors = loansService.addLoan(1L, 1L, List.of(3L), 5L);
        assertTrue(errors.contains("Too many Taladro are loan, cant loan more."));
    }

    @Test
    void testReturnLoan_ToolInBadState() {
        ToolsEntity badTool = new ToolsEntity();
        badTool.setToolId(4L);
        badTool.setToolName("Serrucho");
        badTool.setInitialState("Malo");
        badTool.setLowDmgFee(30L);

        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(4L));
        when(toolsService.getToolsById(4L)).thenReturn(badTool);
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loansRepository.save(any())).thenReturn(loan);

        ReturnLoanDTO dto = loansService.returnLoan(loan);
        assertNotNull(dto);
        assertEquals(30L, dto.getLowDmgAmount());
        assertTrue(dto.getTools().contains("Serrucho"));
    }

    @Test
    void testCalculateRepoFine_WithDamagedTool() {
        ToolsEntity damagedTool = new ToolsEntity();
        damagedTool.setToolId(5L);
        damagedTool.setInitialState("Dañada");
        damagedTool.setRepositionFee(100L);

        when(clientService.getClientById(1L)).thenReturn(client);
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(5L));
        when(toolsService.getToolsById(5L)).thenReturn(damagedTool);
        when(fineService.saveFine(any())).thenReturn(new FineEntity());

        ReturnLoanDTO dto = loansService.calculateRepoFine(1L, 1L);
        assertEquals(100L, dto.getRepoAmount());
        assertNotNull(dto.getRepoFine());
    }

    @Test
    void testCalculateFine_LateReturn() {
        loan.setReturnDate(Date.valueOf(LocalDate.now().minusDays(3)));
        when(loansRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(toolsLoansRepository.findByLoanId(1L)).thenReturn(List.of(1L));
        tool.setDiaryFineFee(50L);
        when(toolsService.getToolsById(1L)).thenReturn(tool);
        when(fineService.saveFine(any())).thenReturn(new FineEntity());

        ReturnLoanDTO dto = loansService.calculateFine(1L, 1L);
        assertTrue(dto.getFineAmount() > 0);
    }


}
