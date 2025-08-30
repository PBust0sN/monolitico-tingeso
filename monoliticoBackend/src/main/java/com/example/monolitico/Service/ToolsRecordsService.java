package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsRecordsEntity;
import com.example.monolitico.Repositories.ToolsRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsRecordsService {

    @Autowired
    ToolsRecordsRepository toolsRecordsRepository;

    //getter of all the relations
    public List<ToolsRecordsEntity> getAllToolsRecords(){
        return (List<ToolsRecordsEntity>) toolsRecordsRepository.findAll();
    }

    //save a ToolsRecords relation into the data base
    public ToolsRecordsEntity saveToolsRecords(ToolsRecordsEntity toolsRecordsEntity){
        return toolsRecordsRepository.save(toolsRecordsEntity);
    }

    //get a relation by his id field
    public ToolsRecordsEntity getToolsRecordsById(Long id){
        return toolsRecordsRepository.findById(id).get();
    }

    //update info of a ToolsRecords
    public ToolsRecordsEntity updateToolsRecords(ToolsRecordsEntity toolsRecordsEntity){
        return toolsRecordsRepository.save(toolsRecordsEntity);
    }

    //delete a client from the data base
    public boolean deleteToolsRecords(Long id) throws Exception{
        try{
            toolsRecordsRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
