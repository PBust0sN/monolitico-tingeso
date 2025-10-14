package com.example.monolitico.Controller;

import com.example.monolitico.Entities.FineReportEntity;
import com.example.monolitico.Service.FineReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fineReport")
@CrossOrigin("*")
public class FineReportController {
    @Autowired
    private FineReportService fineReportService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<List<FineReportEntity>> getFineReportByReportId(@PathVariable("id") Long id){
        List<FineReportEntity> fineReportEntities = fineReportService.getFineReportsByReportId(id);
        return ResponseEntity.ok(fineReportEntities);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<FineReportEntity>> getAllFineReports(){
        List<FineReportEntity> fineReportEntities = fineReportService.getAllFineReport();
        return ResponseEntity.ok(fineReportEntities);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<FineReportEntity> createFineReport(@RequestBody FineReportEntity fineReportEntity){
        FineReportEntity newFineReportEntity = fineReportService.createFineReport(fineReportEntity);
        return ResponseEntity.ok(newFineReportEntity);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<FineReportEntity> deleteFineReport(@PathVariable("id") Long id)throws Exception{
        var isDeleted =  fineReportService.deleteFineReportById(id);
        return ResponseEntity.noContent().build();
    }
}
