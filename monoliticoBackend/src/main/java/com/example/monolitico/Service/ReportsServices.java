package com.example.monolitico.Service;

import com.example.monolitico.Entities.*;
import com.example.monolitico.Repositories.ReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    @Autowired
    private ReportsRepository reportsRepository;

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

    public List<ReportsEntity> getReportsBetweenDates(Date date1, Date date2) {
        return reportsRepository.findByReportDateBetween(date1, date2);
    }

    //STANDARD CRUD

    public ReportsEntity getReportsById(Long id){
        return  reportsRepository.findById(id).get();
    }

    public List<ReportsEntity> getAllReports(){
        return (List<ReportsEntity>) reportsRepository.findAll();
    }

    public ReportsEntity saveReport(ReportsEntity reportsEntity){
        return reportsRepository.save(reportsEntity);
    }

    public ReportsEntity updateReport(ReportsEntity reportsEntity){
        return reportsRepository.save(reportsEntity);
    }

    public boolean deleteReport(Long id) throws Exception{
        try{
            reportsRepository.deleteById(id);
            return true;
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
