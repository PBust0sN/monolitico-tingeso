package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Service.ClientLoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients/loans")
@CrossOrigin("*")
public class ClientLoansController {

    @Autowired
    ClientLoansService clientLoansService;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ResponseEntity<List<ClientLoansEntity>> getAllClients() {
        List<ClientLoansEntity> clientsloans = clientLoansService.getAllClientsLoans();
        return ResponseEntity.ok(clientsloans);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientLoansEntity> getClientById(@PathVariable Long id) {
        ClientLoansEntity clientloansEntity = clientLoansService.getClientLoansById(id);
        return ResponseEntity.ok(clientloansEntity);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ClientLoansEntity> saveClient(@RequestBody ClientLoansEntity clientLoansEntity) {
        ClientLoansEntity newClientLoan = clientLoansService.saveClientLoan(clientLoansEntity);
        return ResponseEntity.ok(newClientLoan);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/")
    public ResponseEntity<ClientLoansEntity> updateClient(@RequestBody ClientLoansEntity clientLoansEntity) {
        ClientLoansEntity updatedClientLoan = clientLoansService.updateClientLoans(clientLoansEntity);
        return ResponseEntity.ok(updatedClientLoan);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientLoansEntity> deleteClient(@PathVariable Long id) throws Exception {
        var isDeleted = clientLoansService.deleteClientLoans(id);
        return ResponseEntity.noContent().build();
    }
}
