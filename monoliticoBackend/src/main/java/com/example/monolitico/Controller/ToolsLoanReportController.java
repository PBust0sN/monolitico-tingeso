package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsLoanReportEntity;
import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Service.ToolsLoanReportService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/toolsloansreports")
@CrossOrigin("*")
public class ToolsLoanReportController {

    @Autowired
    private ToolsLoanReportService toolsLoanReportService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/getools/{id}")
    public ResponseEntity<List<Long>> getByLoanId(@PathVariable Long id) {
        List<Long> tools = toolsLoanReportService.findByLoanId(id);
        return ResponseEntity.ok(tools);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ToolsLoanReportEntity> create(@RequestBody ToolsLoanReportEntity toolsLoanReportEntity) {
        ToolsLoanReportEntity newToolsLoanReport = toolsLoanReportService.createToolsLoanReport(toolsLoanReportEntity);
        return ResponseEntity.ok(newToolsLoanReport);
    }
}
