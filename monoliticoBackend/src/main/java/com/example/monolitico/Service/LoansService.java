package com.example.monolitico.Service;

import com.example.monolitico.Entities.*;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoansService {
    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    FineService fineService;

    @Autowired
    ToolsService toolsService;

    @Autowired
    ToolsLoansRepository toolsLoansRepository;

    @Autowired
    ToolsLoansService toolsLoansService;
    @Autowired
    private RecordsServices recordsServices;

    //getter of all loans
    public List<LoansEntity> getAllLoans() {
        return (List<LoansEntity>) loansRepository.findAll();
    }

    //save a loan into the data base
    public Optional<LoansEntity> saveLoan(LoansEntity loansEntity) {
        if (loansEntity.getReturnDate() != null) {
            return Optional.of(loansRepository.save(loansEntity));
        }
        return Optional.empty();
    }


    public List<String> AddLoan(Long staff_id, Long client_id, List<Long> tools_ids, Long days) {
        List<String> errors = new ArrayList<>();

        //verify that the client is allowed to have another loan
        ClientEntity client = clientService.getClientById(client_id);
        // we create a loan
        LoansEntity loansEntity = new LoansEntity();

        //set the date
        LocalDateTime date = LocalDateTime.now();
        loansEntity.setDate(Date.valueOf(date.toLocalDate().toString()));

        //set deliverydate
        loansEntity.setDeliveryDate(Date.valueOf(date.toLocalDate().toString()));

        //set the return date
        loansEntity.setReturnDate(Date.valueOf(date.plusDays(days).toLocalDate().toString()));
        //if avaliable is true then the client is allowed for another loan
        if (client.getAvaliable()
                && !fineService.hasFinesByClientId(client_id)
                && !fineService.hasFinesOfToolReposition(client_id)
                && checkDates(loansEntity)) {

            loansEntity.setLoanType("prestamo");
            loansEntity.setStaffId(staff_id);

            Long amount = 0L;
            //we add the tools in the list to the tools loans table
            for (Long tool_id : tools_ids) {
                ToolsEntity tool = toolsService.getToolsById(tool_id);

                amount += tool.getLoanFee();

                //we substract the stock of the tools
                tool.setInitialState("Prestada");

                //we add the relation in the tool_loan table
                ToolsLoansEntity toolsLoansEntity = new ToolsLoansEntity();
                toolsLoansEntity.setToolId(tool_id);
                toolsLoansEntity.setLoanId(loansEntity.getLoanId());
                toolsLoansService.saveToolsLoans(toolsLoansEntity);

                //we update the tool info
                toolsService.updateTool(tool);
            }

            loansEntity.setAmount(amount);

            saveLoan(loansEntity);

            //we have to generate a new move in the kardex
            RecordsEntity record =  new RecordsEntity();
            record.setRecordType("Loan");

            //obtenemos la hora actual
            record.setRecordDate(Date.valueOf(date.toLocalDate().toString())); // hora actual
            record.setLoanId(loansEntity.getLoanId());
            record.setClientId(client_id);
        }
        errors.add("Client is not avaliable or has fines");
        return errors;
    }

    // verify date of loan
    public boolean checkDates(LoansEntity loan) {
        //formating the delivery date
        String deliveryDate = loan.getDeliveryDate().toString();
        Date sqlDate1 = Date.valueOf(deliveryDate);
        LocalDate localDate1 = sqlDate1.toLocalDate();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(localDate1, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 loan behind
        if (dias < 0) {
            return false;
        }
        return true;
    }

    public Optional<LoansEntity> returnLoan(LoansEntity loansEntity) {
        //if its returned before the return date, then the client don't gets his money back
        Long repoAmount = 0L;
        Long fineAmount = 0L;
        if (reamaningDaysOnLoan(loansEntity.getLoanId()) > 0) {
            repoAmount = calculateRepoFine(loansEntity.getLoanId());
        } else if (reamaningDaysOnLoan(loansEntity.getLoanId()) < 0) {
            fineAmount = calculateFine(loansEntity.getLoanId());
            repoAmount = calculateRepoFine(loansEntity.getLoanId());
        }
        //obtenemos la hora actual
        LocalDateTime date = LocalDateTime.now();
        loansEntity.setDate(Date.valueOf(date.toLocalDate().toString()));
        loansEntity.setExtraCharges(repoAmount + fineAmount);

        //generate a kardex move
        //we have to generate a new move in the kardex
        RecordsEntity record =  new RecordsEntity();
        record.setRecordType("Return");

        record.setRecordAmount(repoAmount+fineAmount);
        record.setRecordDate(Date.valueOf(date.toLocalDate().toString())); // hora actual
        record.setLoanId(loansEntity.getLoanId());
        record.setClientId(loansEntity.getClientId());

        recordsServices.saveRecord(record);

        //we add up the stock for every one of the tools in the loan
        //fist we have to check the conditions the tools return to the shop
        List<Long> toolsId = toolsLoansRepository.findByLoanId(loansEntity.getLoanId());

        //Disponible, Prestada, En reparación, Dada de baja (STATES)
        for (Long toolId : toolsId) {
            ToolsEntity toolsEntity = toolsService.getToolsById(toolId);

            String toolDisponibility = toolsEntity.getDisponibility();
            //if the tool is damaged, then we update the initial state of the
            if(toolDisponibility.equals("Dañada")){
                toolsEntity.setInitialState("En reparacion");
                toolsService.updateTool(toolsEntity);
            }else{
                //the stock of a tool is determinated by the amount of tools of the same name, that have
                //the initial state in disponible
                toolsEntity.setInitialState("Disponible");
                toolsService.updateTool(toolsEntity);
            }


        }


        return saveLoan(loansEntity);
    }

    public Long calculateRepoFine(Long id){
        List<Long> tools = toolsLoansRepository.findByLoanId(id);
        Long repofine = 0L;
        for(Long toolId : tools){

            repofine += toolsService.getToolsById(toolId).getRepositionFee();
        }
        return repofine;
    }

    //verify that the loan ins't returned before its return date
    public Long reamaningDaysOnLoan(Long id){
        LoansEntity loan = loansRepository.findById(id).get();
        //formating the delivery date
        String deliveryDate = loan.getDeliveryDate().toString();
        Date sqlDate1 = Date.valueOf(deliveryDate);
        LocalDate localDate1 = sqlDate1.toLocalDate();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(localDate1, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 day behind
        return dias;
    }



    //calculateFine
    public Long calculateFine(Long id){
        if(reamaningDaysOnLoan(id)<0){
            //if its return after, them we calculate the fine
            //we need to get all the tools in the loan
            List<Long> tools = toolsLoansRepository.findByLoanId(id);
            Long fine = 0L;

            for(Long toolId : tools){
                //Multa atraso = días de atraso × tarifa diaria de multa.
                //per tool
                fine += reamaningDaysOnLoan(id) * toolsService.getToolsById(toolId).getDiaryFineFee();
            }
            return fine;
        }
        return 0L;
    }

    //get a loan by its id field
    public LoansEntity findLoanById(Long id){
        return loansRepository.findById(id).get();
    }

    //update info of a loan
    public LoansEntity updateLoan(LoansEntity loansEntity){
        return loansRepository.save(loansEntity);
    }

    //delete a loan from the database
    public boolean deleteLoan(Long id) throws Exception{
        try{
            loansRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
