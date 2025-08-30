package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Repositories.ClientLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientLoansService {

    @Autowired
    ClientLoansRepository clientLoansRepository;

    //getter of all the relations
    public List<ClientLoansEntity> getAllClientsLoans(){
        return (List<ClientLoansEntity>) clientLoansRepository.findAll();
    }

    //save a clientloan relation into the data base
    public ClientLoansEntity saveClientLoan(ClientLoansEntity clientLoansEntity){
        return clientLoansRepository.save(clientLoansEntity);
    }

    //get a relation by his id field
    public ClientLoansEntity getClientLoansById(Long id){
        return clientLoansRepository.findById(id).get();
    }

    //update info of a client
    public ClientLoansEntity updateClientLoans(ClientLoansEntity clientLoansEntity){
        return clientLoansRepository.save(clientLoansEntity);
    }

    //delete a client from the data base
    public boolean deleteClientLoans(Long id) throws Exception{
        try{
            clientLoansRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
