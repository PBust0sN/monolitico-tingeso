package com.example.monolitico.Controller;


import com.example.monolitico.Entities.ClientBehindEntity;
import com.example.monolitico.Service.ClientBehindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientsBehind")
@CrossOrigin("*")
public class ClientBehindController {
    @Autowired
    private ClientBehindService clientBehindService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientBehindEntity> getClientBehindByReportId(@PathVariable("id") Long reportId){
        ClientBehindEntity clientsBehind = clientBehindService.getClientBehindByReportId(reportId);
        return ResponseEntity.ok(clientsBehind);
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientBehindEntity>> getAllClientBehindByReportId(){
        List<ClientBehindEntity> clientsBehind = clientBehindService.getAllClientBehind();
        return ResponseEntity.ok(clientsBehind);
    }

    @PostMapping("/")
    public ResponseEntity<ClientBehindEntity> createClientBehind(@RequestBody ClientBehindEntity clientBehindEntity){
        ClientBehindEntity newClientBehindEntity = clientBehindService.createClientBehind(clientBehindEntity);
        return ResponseEntity.ok(newClientBehindEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientBehindEntity> deleteClientBehindById(@PathVariable("id") Long id) throws Exception{
        var isDeleted = clientBehindService.deleteClientBehindById(id);
        return ResponseEntity.noContent().build();
    }
}
