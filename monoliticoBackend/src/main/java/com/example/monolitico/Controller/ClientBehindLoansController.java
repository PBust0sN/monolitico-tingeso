package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ClientBehindLoansEntity;
import com.example.monolitico.Service.ClientBehindLoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientsBehindLoans")
@CrossOrigin("*")
public class ClientBehindLoansController {

    @Autowired
    private ClientBehindLoansService  clientBehindLoansService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<List<Long>> findLoansIdByClientIdBehind (@PathVariable("id") Long clientIdBehind) {
        List<Long> clientids = clientBehindLoansService.findClientIdByClientIdBehind(clientIdBehind);
        return ResponseEntity.ok(clientids);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @PostMapping("/")
    public ResponseEntity<ClientBehindLoansEntity> createCBL(@RequestBody ClientBehindLoansEntity  clientBehindLoansEntity){
        ClientBehindLoansEntity newClientBehindLoansEntity = clientBehindLoansService.createCBL(clientBehindLoansEntity);
        return  ResponseEntity.ok(newClientBehindLoansEntity);
    }
}
