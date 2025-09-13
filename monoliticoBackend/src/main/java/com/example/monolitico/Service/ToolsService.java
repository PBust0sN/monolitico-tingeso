package com.example.monolitico.Service;

import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ToolsService {

    @Autowired
    ToolsRepository toolsrepository;

    @Autowired
    RecordsServices recordservice;

    @Autowired
    StaffService staffService;

    //getter of al the tools
    public List<ToolsEntity> getAllTools(){
        return (List<ToolsEntity>) toolsrepository.findAll();
    }

    //save a tool into the database
    public Optional<ToolsEntity> saveTool(ToolsEntity toolsEntity){
        //has to have a name
        if(toolsEntity.getToolName()!=null) {
            //has to have category
            if (toolsEntity.getCategory() != null) {
                //has to have a reposition fee
                if (toolsEntity.getRepositionFee() != null) {
                    //has to be a valid state
                    if(toolsEntity.getInitialState()=="disponible"
                    || toolsEntity.getInitialState()=="prestada"
                    || toolsEntity.getInitialState()=="en reparacion"
                    || toolsEntity.getInitialState()=="dada de baja"){
                        //generate a new record (records)
                        RecordsEntity record = new RecordsEntity();
                        LocalDate diaActual = LocalDate.now();
                        Date sqlDate = Date.valueOf(diaActual);
                        record.setRecordDate(sqlDate);
                        //record_types (registro nuevas herramientas, préstamo, devolución, baja, reparación)
                        record.setRecordType("registro nuevas herramientas");

                        //save the record
                        recordservice.saveRecord(record);
                        //save the tool
                        return Optional.of(toolsrepository.save(toolsEntity));
                    }

                }
            }
        }
        //if none of the conditions is valid or at least one of them aren't valid then
        //return empty value as a sign of warning, (tool can't be saved)
        return Optional.empty();
    }

    //get a tool by its id field
    public ToolsEntity getToolsById(Long id){
        return toolsrepository.findById(id).get();
    }

    //update info of a tool
    public ToolsEntity updateTool(ToolsEntity toolsEntity){
        return toolsrepository.save(toolsEntity);
    }

    //only an admin can drop down a tool in the system
    public Optional<ToolsEntity> dropDownATool(Long requester_id, Long tool_id){
        StaffEntity staff = staffService.getStaffById(requester_id);
        //if the requester is an admin then
        //find the tool
        ToolsEntity tool = toolsrepository.findById(tool_id).get();
        //change its initial state
        tool.setInitialState("dada de baja");
        //update the tool in the data base
        return Optional.of(updateTool(tool));
    }

    //delete a tool from the database
    public boolean deleteTools(Long id) throws Exception{
        try{
            toolsrepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean hasDisponibilityAndStock(Long id){
        //first we find the tool by id
        ToolsEntity tool = toolsrepository.findById(id).get();
        if(tool.getDisponibility().equals("Disponible") && tool.getStock()>= 1){
            return true;
        }
        return false;
    }


}
