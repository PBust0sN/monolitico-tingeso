package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsRecordsEntity;
import com.example.monolitico.Service.ToolsRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools/records")
@CrossOrigin("*")
public class ToolsRecordsController {

    @Autowired
    ToolsRecordsService toolsRecordsService;

    @GetMapping("/")
    public ResponseEntity<List<ToolsRecordsEntity>> getAllToolsRecords() {
        List<ToolsRecordsEntity> toolsRecordsEntity = toolsRecordsService.getAllToolsRecords();
        return ResponseEntity.ok(toolsRecordsEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolsRecordsEntity> getToolsRecordsById(@PathVariable Long id) {
        ToolsRecordsEntity toolsRecordsEntity = toolsRecordsService.getToolsRecordsById(id);
        return ResponseEntity.ok(toolsRecordsEntity);
    }

    @PostMapping("/")
    public ResponseEntity<ToolsRecordsEntity> saveToolsRecords(@RequestBody ToolsRecordsEntity toolsRecordsEntity) {
        ToolsRecordsEntity newToolsRecordsEntity = toolsRecordsService.saveToolsRecords(toolsRecordsEntity);
        return ResponseEntity.ok(newToolsRecordsEntity);
    }

    @PutMapping("/")
    public ResponseEntity<ToolsRecordsEntity> updateToolsRecords(@RequestBody ToolsRecordsEntity toolsRecordsEntity) {
        ToolsRecordsEntity updatedToolsRecordsEntity = toolsRecordsService.updateToolsRecords(toolsRecordsEntity);
        return ResponseEntity.ok(updatedToolsRecordsEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ToolsRecordsEntity> deleteToolsRecords(@PathVariable Long id) throws Exception {
        var isDeleted = toolsRecordsService.deleteToolsRecords(id);
        return ResponseEntity.noContent().build();
    }
}
