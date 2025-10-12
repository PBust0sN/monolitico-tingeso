package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientLoansRepository clientLoansRepository;

    @Autowired
    private KeycloakService keycloakService;
    @Autowired
    private ToolsLoansRepository toolsLoansRepository;

    //Getter of all clients
    public List<ClientEntity> getAllClients(){
        return (List<ClientEntity>)  clientRepository.findAll();
    }

    //save a client into the data base
    public ClientEntity saveClient(ClientEntity clientEntity){
        ClientEntity client = clientRepository.save(clientEntity);
        keycloakService.createUserInKeycloak(
                client.getName(),
                client.getMail(),
                client.getPassword(),
                client.getClient_id()
        );
        return client;
    }

    //get a client by his id field
    public ClientEntity getClientById(Long id){
        return clientRepository.findById(id).get();
    }

    //update info of a client
    public ClientEntity updateClient(ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    //delete a client from the data base
    public boolean deleteClient(Long id) throws Exception{
        try{
            clientRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    //calculate if the client has expired loans
    public boolean hasExpiredLoansById(Long id){
        //first we find all teh pending loans of a client
        List<LoansEntity> loans = clientRepository.getAllLoansByClientId(id);

        System.out.println("Loans: " + loans);
        if(loans.isEmpty()){
            System.out.println("No hay loans en el cliente");
            return false;
        }
        //then we substract the return date minus delivery date
        for(LoansEntity loan : loans){
            //formating the delivery date
            LocalDate localDate1 = loan.getDeliveryDate().toLocalDate();

            //formating the return date

            LocalDate localDate2 = loan.getReturnDate().toLocalDate();

            long dias = ChronoUnit.DAYS.between(localDate1, localDate2);
            //if the differrence is negative it means the loan is late
            //therefore, the client has at least 1 loan behind
            if(dias<0){
                return true;
            }
        }
        //if all the loans are up to date, then the client hasn't loans behind
        return false;
    }

    public boolean HasTheSameToolInLoanByClientId(Long clientId, Long toolId){
        List<Long> activeLoans = clientLoansRepository.findLoansIdsByClientId(clientId);

        for(Long loanId : activeLoans){
            List<Long> tools = toolsLoansRepository.findByLoanId(loanId);
            for(Long toolId1 : tools){
                if(toolId1.equals(toolId)){
                    return true;
                }
            }
        }
        return false;
    }

    public ClientEntity findByRut (String rut){
        return clientRepository.findByRut(rut);
    }
}
