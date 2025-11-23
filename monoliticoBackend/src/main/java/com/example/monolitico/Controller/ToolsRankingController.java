package com.example.monolitico.Controller;

import com.example.monolitico.Entities.ToolsRankingEntity;
import com.example.monolitico.Service.ToolsRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/toolsRanking")
@CrossOrigin("*")
public class ToolsRankingController {
    @Autowired
    private ToolsRankingService toolsRankingService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<List<ToolsRankingEntity>> getToolsRankingById(@PathVariable("id") Long id) {
        List<ToolsRankingEntity> toolsRankingEntities = toolsRankingService.getToolsRankingByReportId(id);
        return ResponseEntity.ok(toolsRankingEntities);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @GetMapping("/")
    public ResponseEntity<List<ToolsRankingEntity>> getAllToolsRanking(){
        List<ToolsRankingEntity>  toolsRankingEntities = toolsRankingService.getAllToolsRanking();
        return ResponseEntity.ok(toolsRankingEntities);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','CLIENT')")
    @PostMapping("/")
    public ResponseEntity<ToolsRankingEntity> createToolsRanking(@RequestBody ToolsRankingEntity toolsRankingEntity){
        ToolsRankingEntity newToolsRanking =  toolsRankingService.createToolsRanking(toolsRankingEntity);
        return ResponseEntity.ok(newToolsRanking);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public  ResponseEntity<ToolsRankingEntity> deleteToolsRanking(@PathVariable("id") Long id) throws Exception{
        var isDeleted =  toolsRankingService.deleteToolsRankingById(id);
        return ResponseEntity.noContent().build();
    }
}
