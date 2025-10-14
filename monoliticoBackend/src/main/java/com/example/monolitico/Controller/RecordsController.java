package com.example.monolitico.Controller;

import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Service.RecordsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@CrossOrigin("*")
public class RecordsController {
    @Autowired
    RecordsServices recordsServices;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<RecordsEntity>> getAllRecords(){
        List<RecordsEntity> records = recordsServices.getAllRecords();
        return ResponseEntity.ok(records);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RecordsEntity> getRecordById(@PathVariable Long id){
        RecordsEntity recordsEntity = recordsServices.getRecordsById(id);
        return ResponseEntity.ok(recordsEntity);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PostMapping("/")
    public ResponseEntity<RecordsEntity> saveRecord(@RequestBody RecordsEntity recordsEntity){
        RecordsEntity newRecord = recordsServices.saveRecord(recordsEntity);
        return ResponseEntity.ok(newRecord);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @PutMapping("/")
    public ResponseEntity<RecordsEntity> updateRecord(@RequestBody RecordsEntity recordsEntity){
        RecordsEntity updateRecord = recordsServices.updateRecord(recordsEntity);
        return ResponseEntity.ok(updateRecord);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @GetMapping("/{date1}/{date2}")
    public ResponseEntity<List<RecordsEntity>> getRecordsByDate(@PathVariable Date date1, @PathVariable Date date2){
        List<RecordsEntity> records = recordsServices.findByRecordDatesBetween(date1,date2);
        return ResponseEntity.ok(records);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @DeleteMapping("/{id}")
    public  ResponseEntity<RecordsEntity> deleteRecord(@PathVariable Long id)throws Exception{
        var isDeleted = recordsServices.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }

}
