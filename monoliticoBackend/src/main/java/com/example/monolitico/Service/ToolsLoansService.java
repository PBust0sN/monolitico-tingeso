package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsLoansEntity;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsLoansService {
    @Autowired
    private ToolsLoansRepository toolsLoansRepository;

    //getter of all fee's
    public List<ToolsLoansEntity> getAllToolsLoans(){
        return (List<ToolsLoansEntity>) toolsLoansRepository.findAll();
    }

    //save a fee unto the data base
    public ToolsLoansEntity saveToolsLoans(ToolsLoansEntity toolsLoansEntity){
        return toolsLoansRepository.save(toolsLoansEntity);
    }

    //get a fee by its id field
    public ToolsLoansEntity getToolsLoansById(Long id){
        return toolsLoansRepository.findById(id).get();
    }

    //update info of a fee
    public ToolsLoansEntity updateToolsLoans(ToolsLoansEntity toolsLoansEntity){
        return toolsLoansRepository.save(toolsLoansEntity);
    }

    //delete a fee from the data base
    public boolean deleteToolsLoans(Long id) throws Exception{
        try{
            toolsLoansRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Long> getToolsIDsByLoanId(Long id){
        return  toolsLoansRepository.findByLoanId(id);
    }
}
