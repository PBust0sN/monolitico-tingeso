package com.example.monolitico.Service;

import com.example.monolitico.Controller.RecordsController;
import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Repositories.RecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class RecordsServices {

    @Autowired
    private RecordsRepository recordsRepository;

    //getter of all records
    public List<RecordsEntity> getAllRecords(){
        return (List<RecordsEntity>) recordsRepository.findAll();
    }

    //save a record into the data base
    public RecordsEntity saveRecord(RecordsEntity recordsEntity) {
        return recordsRepository.save(recordsEntity);
    }

    //get a record by its id field
    public RecordsEntity getRecordsById(Long id){
        return recordsRepository.findById(id).get();
    }

    //update info of a record
    public RecordsEntity updateRecord(RecordsEntity recordsEntity){
        return recordsRepository.save(recordsEntity);
    }

    //delete a record from the data base
    public boolean deleteRecord(Long id) throws  Exception{
        try{
            recordsRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    //get records between dates
    public List<RecordsEntity> findByRecordDatesBetween(Date start_date, Date end_date){
        return recordsRepository.findByRecordDateBetween(start_date,end_date);
    }
}
