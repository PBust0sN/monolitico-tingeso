package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Service.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "*")
public class ToolsController {
    @Autowired
    ToolsService toolsService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<ToolsEntity>> getAllTools() {
        List<ToolsEntity> tools = toolsService.getAllTools();
        return ResponseEntity.ok(tools);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ToolsEntity> getToolById(@PathVariable Long id) {
        ToolsEntity toolsEntity = toolsService.getToolsById(id);
        return ResponseEntity.ok(toolsEntity);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Optional<ToolsEntity>> saveTool(@RequestBody ToolsEntity toolsEntity) {
        Optional<ToolsEntity> newTool =  toolsService.saveTool(toolsEntity);
        return ResponseEntity.ok(newTool);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/")
    public  ResponseEntity<ToolsEntity> updateTool(@RequestBody ToolsEntity toolsEntity) {
        ToolsEntity updateTool =  toolsService.updateTool(toolsEntity);
        return ResponseEntity.ok(updateTool);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/dropdown")
    public ResponseEntity<Optional<ToolsEntity>> dropdownTool(@RequestParam Long requester_id, @RequestParam Long tool_id) {
        Optional<ToolsEntity> updatedTool = toolsService.dropDownATool(requester_id,tool_id);
        return ResponseEntity.ok(updatedTool);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ToolsEntity> deleteTool(@PathVariable Long id) throws Exception {
        var isDeleted = toolsService.deleteTools(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/update/state/{id}")
    public ResponseEntity<ToolsEntity> updateToolState(@PathVariable Long id, @RequestParam String state){
        ToolsEntity updateTool =  toolsService.updateStateById(id, state);
        return ResponseEntity.ok(updateTool);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/update/stock/{id}")
    public  ResponseEntity<ToolsEntity> updateToolStock(@PathVariable Long id){
        ToolsEntity updateTool = toolsService.updateStockById(id);
        return ResponseEntity.ok(updateTool);
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @GetMapping("/topTen")
    public ResponseEntity<List<ToolsEntity>> getTopTenToolsLoanCount() {
        List<ToolsEntity> tenTools = toolsService.findRankingMax10();
        return ResponseEntity.ok(tenTools);
    }
}
