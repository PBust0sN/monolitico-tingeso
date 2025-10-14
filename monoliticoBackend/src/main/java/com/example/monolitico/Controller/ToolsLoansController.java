package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsLoansEntity;
import com.example.monolitico.Service.ToolsLoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools/loan")
@CrossOrigin("*")
public class ToolsLoansController {
    @Autowired
    ToolsLoansService toolsLoansService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<ToolsLoansEntity>> getAllToolsLoans(){
        List<ToolsLoansEntity> feeEntities = toolsLoansService.getAllToolsLoans();
        return ResponseEntity.ok(feeEntities);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ToolsLoansEntity> getToolsLoansById(@PathVariable Long id){
        ToolsLoansEntity toolsLoansEntity = toolsLoansService.getToolsLoansById(id);
        return ResponseEntity.ok(toolsLoansEntity);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ToolsLoansEntity> saveToolsLoans(@RequestBody ToolsLoansEntity toolsLoansEntity){
        ToolsLoansEntity newFee = toolsLoansService.saveToolsLoans(toolsLoansEntity);
        return ResponseEntity.ok(newFee);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/")
    public  ResponseEntity<ToolsLoansEntity> updateToolsLoans(@RequestBody ToolsLoansEntity toolsLoansEntity){
        ToolsLoansEntity updateFee = toolsLoansService.updateToolsLoans(toolsLoansEntity);
        return ResponseEntity.ok(updateFee);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ToolsLoansEntity> deleteToolsLoans(@PathVariable Long id) throws Exception{
        var isDeleted = toolsLoansService.deleteToolsLoans(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/toolsIDs/{id}")
    public ResponseEntity<List<Long>> getToolsIDsByLoanId(@PathVariable Long id){
        List<Long> toolsIDs = toolsLoansService.getToolsIDsByLoanId(id);
        return ResponseEntity.ok(toolsIDs);
    }
}
