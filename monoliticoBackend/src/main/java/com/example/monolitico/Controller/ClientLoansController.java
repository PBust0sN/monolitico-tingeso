package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Service.ClientLoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients/loans")
@CrossOrigin("*")
public class ClientLoansController {

    @Autowired
    ClientLoansService clientLoansService;

    @GetMapping("/")
    public ResponseEntity<List<ClientLoansEntity>> getAllClients() {
        List<ClientLoansEntity> clientsloans = clientLoansService.getAllClientsLoans();
        return ResponseEntity.ok(clientsloans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientLoansEntity> getClientById(@PathVariable Long id) {
        ClientLoansEntity clientloansEntity = clientLoansService.getClientLoansById(id);
        return ResponseEntity.ok(clientloansEntity);
    }

    @PostMapping("/")
    public ResponseEntity<ClientLoansEntity> saveClient(@RequestBody ClientLoansEntity clientLoansEntity) {
        ClientLoansEntity newClientLoan = clientLoansService.saveClientLoan(clientLoansEntity);
        return ResponseEntity.ok(newClientLoan);
    }

    @PutMapping("/")
    public ResponseEntity<ClientLoansEntity> updateClient(@RequestBody ClientLoansEntity clientLoansEntity) {
        ClientLoansEntity updatedClientLoan = clientLoansService.updateClientLoans(clientLoansEntity);
        return ResponseEntity.ok(updatedClientLoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientLoansEntity> deleteClient(@PathVariable Long id) throws Exception {
        var isDeleted = clientLoansService.deleteClientLoans(id);
        return ResponseEntity.noContent().build();
    }
}
