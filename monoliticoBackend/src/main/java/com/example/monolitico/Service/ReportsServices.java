package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Entities.ToolsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsServices {
    @Autowired
    LoansService loansService;

    @Autowired
    ClientService clientService;

    @Autowired
    ToolsService toolsService;

    public List<LoansEntity> generateLoansReport(){
        List<LoansEntity> loans = new ArrayList<>();


        return loans;
    }

    public List<ClientEntity> generateBehindClientsReport(){
        List<ClientEntity> clients = new ArrayList<>();

        return clients;
    }

    public List<ToolsEntity> generatedMostLoanToolsReport(){
        List<ToolsEntity> tools = new ArrayList<>();

        return tools;
    }
}
