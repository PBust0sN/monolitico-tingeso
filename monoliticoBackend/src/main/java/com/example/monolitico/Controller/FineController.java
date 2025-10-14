package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.FineEntity;
import com.example.monolitico.Repositories.FineRepository;
import com.example.monolitico.Service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fine")
@CrossOrigin("*")
public class FineController {
    @Autowired
    private FineService fineService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<FineEntity> getFineById(@PathVariable Long id){
        FineEntity fine = fineService.getFineById(id);
        return ResponseEntity.ok(fine);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<FineEntity>> getAllFine(){
        List<FineEntity> fines = fineService.getAllFine();
        return ResponseEntity.ok(fines);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<FineEntity> createFine(@RequestBody FineEntity fineEntity){
        FineEntity newFine = fineService.saveFine(fineEntity);
        return ResponseEntity.ok(newFine);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/")
    public ResponseEntity<FineEntity> updateFine(@RequestBody FineEntity fineEntity){
        FineEntity updatefine = fineService.updateFine(fineEntity);
        return ResponseEntity.ok(updatefine);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<FineEntity> deleteFine(@PathVariable Long id) throws Exception {
        var isDeleted = fineService.deleteFineById(id);
        return ResponseEntity.noContent().build();

    }
}
