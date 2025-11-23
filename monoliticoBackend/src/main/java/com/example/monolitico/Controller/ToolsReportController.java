package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsReportEntity;
import com.example.monolitico.Service.ToolsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/toolsReport")
@CrossOrigin("*")
public class ToolsReportController {

    @Autowired
    private ToolsReportService   toolsReportService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ToolsReportEntity> getToolsReportById(@PathVariable Long id){
        ToolsReportEntity tool = toolsReportService.getByToolId(id);
        return ResponseEntity.ok(tool);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @PostMapping("/")
    public ResponseEntity<ToolsReportEntity> saveToolsReport(@RequestBody ToolsReportEntity tool){
        ToolsReportEntity newToolReport = toolsReportService.createToolReport(tool);
        return ResponseEntity.ok(newToolReport);
    }
}
