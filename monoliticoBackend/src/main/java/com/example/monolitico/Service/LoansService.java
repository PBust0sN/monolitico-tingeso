package com.example.monolitico.Service;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Entities.RecordsEntity;
import com.example.monolitico.Entities.ToolsEntity;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    // ///////////////////////////////////////////////// FALTA POR TERMINAR
    public Optional<LoansEntity> AddLoan(Long client_id) {
        //verify that the client is allowed to have another loan
        ClientEntity client = clientService.getClientById(client_id);
        // we create a loan
        LoansEntity loansEntity = new LoansEntity();

        //if avaliable is true then the client is allowed for another loan
        if (client.getAvaliable()
                && !fineService.hasFinesByClientId(client_id)
                && !fineService.hasFinesOfToolReposition(client_id)
                && checkDates(loansEntity)) {


            //we have to generate a new move in the kardex
            RecordsEntity record =  new RecordsEntity();
            record.setRecordType("Loan");

            //obtenemos la hora actual
            LocalDateTime date = LocalDateTime.now();
            record.setRecordDate(Date.valueOf(date.toLocalDate().toString())); // hora actual
            record.setLoanId(loansEntity.getLoanId());
            record.setClientId(client_id);


            //we substract the stock of the tools


            return saveLoan(loansEntity);
        }

        return Optional.empty();
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
