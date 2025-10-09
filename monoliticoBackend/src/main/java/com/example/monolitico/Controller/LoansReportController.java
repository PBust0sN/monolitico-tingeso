package com.example.monolitico.Controller;

import com.example.monolitico.Entities.LoansReportEntity;
import com.example.monolitico.Service.LoansReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loansReport")
@CrossOrigin("*")
public class LoansReportController {
    @Autowired
    private LoansReportService loansReportService;

    @GetMapping("/{id}")
    public ResponseEntity<List<LoansReportEntity>> getLoansReportByReportId(@PathVariable("id") Long reportId){
        List<LoansReportEntity>  loansReportEntities = loansReportService.getLoansReportByReportId(reportId);
        return ResponseEntity.ok(loansReportEntities);
    }

    @GetMapping("/")
    public ResponseEntity<List<LoansReportEntity>> getAllLoansReport(){
        List<LoansReportEntity> loansReportEntities = loansReportService.getAllLoansReport();
        return ResponseEntity.ok(loansReportEntities);
    }

    @GetMapping("/getbyid/{id}")
    public  ResponseEntity<LoansReportEntity> getLoansReportById(@PathVariable("id") Long reportId){
        LoansReportEntity loan = loansReportService.getLoansReportById(reportId);
        return ResponseEntity.ok(loan);
    }


    @PostMapping("/")
    public ResponseEntity<LoansReportEntity> createLoansReport(@RequestBody LoansReportEntity loansReportEntity){
        LoansReportEntity createLoansReportEntity = loansReportService.createLoansReport(loansReportEntity);
        return ResponseEntity.ok(createLoansReportEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoansReportEntity> deleteLoansReport(@PathVariable("id") Long id)throws Exception{
        var isDeleted =  loansReportService.deleteLoansReportById(id);
        return ResponseEntity.noContent().build();
    }
}
