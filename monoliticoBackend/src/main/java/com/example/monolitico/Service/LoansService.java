package com.example.monolitico.Service;

import com.example.monolitico.DTO.ReturnLoanDTO;
import com.example.monolitico.Entities.*;
import com.example.monolitico.Repositories.LoansRepository;
import com.example.monolitico.Repositories.ToolsLoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        System.out.println(staff_id);
        System.out.println(client_id);
        System.out.println(tools_ids);
        System.out.println(days);
        //verify that the client is allowed to have another loan
        ClientEntity client = clientService.getClientById(client_id);
        //if avaliable is true then the client is allowed for another loan
        if (client.getAvaliable()
                && !fineService.hasFinesByClientId(client_id)
                && !fineService.hasFinesOfToolReposition(client_id)
                && !clientService.hasExpiredLoansById(client_id)) {

            // we create a loan
            LoansEntity loansEntity = new LoansEntity();

            //set the date
            LocalDateTime date = LocalDateTime.now();
            loansEntity.setDate(Date.valueOf(date.toLocalDate().toString()));

            //set deliverydate
            loansEntity.setDeliveryDate(Date.valueOf(date.toLocalDate().toString()));

            //set the return date
            loansEntity.setReturnDate(Date.valueOf(date.plusDays(days).toLocalDate().toString()));

            loansEntity.setActive(true);
            //if the dates entered in the newloan are wrong, then throw error
            if(!checkDates(loansEntity)) {
                errors.add("dates are place bad");
            }else{
                loansEntity.setLoanType("loan   ");
                loansEntity.setStaffId(staff_id);
                System.out.println("cliente: " + client);
                loansEntity.setClientId(client_id);
                loansEntity.setExtraCharges(0L);

                Long amount = 0L;
                //we add the tools in the list to the tools loans table
                for (Long tool_id : tools_ids) {
                    ToolsEntity tool = toolsService.getToolsById(tool_id);

                    //if the client has the same tool already loan to him then throw a error
                    if(clientService.HasTheSameToolInLoanByClientId(client_id, tool_id)){
                        errors.add("you have a loan with: ".concat(tool.getToolName()));
                    }else {
                        amount += tool.getLoanFee();
                    }
                }
                //if we have encountered 0 errors then we continue the process
                if(errors.isEmpty()){
                    loansEntity.setAmount(amount);

                    saveLoan(loansEntity);

                    for (Long tool_id : tools_ids) {
                        ToolsEntity tool = toolsService.getToolsById(tool_id);

                        //we substract the stock of the tools
                        tool.setStock(tool.getStock() - 1);

                        //we add the relation in the tool_loan table
                        ToolsLoansEntity toolsLoansEntity = new ToolsLoansEntity();
                        toolsLoansEntity.setToolId(tool_id);
                        toolsLoansEntity.setLoanId(loansEntity.getLoanId());
                        toolsLoansService.saveToolsLoans(toolsLoansEntity);

                        //we update the tool info
                        toolsService.updateTool(tool);
                    }

                    //we have to generate a new move in the kardex
                    RecordsEntity record =  new RecordsEntity();
                    record.setRecordType("Loan");

                    //we get teh actual time
                    record.setRecordDate(Date.valueOf(date.toLocalDate().toString())); // actual date
                    record.setLoanId(loansEntity.getLoanId());
                    record.setClientId(client_id);
                    System.out.println(errors);
                    return errors;
                }
            }
        }else{
            errors.add("Client is not avaliable or has fines");
            System.out.println(errors);
            return errors;
        }
        return errors;
    }

    // verify date of loan
    public boolean checkDates(LoansEntity loan) {
        // Fecha actual
        LocalDate today = LocalDate.now();

        //formating the return date

        LocalDate localDate = loan.getReturnDate().toLocalDate();

        long dias = ChronoUnit.DAYS.between(today, localDate);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 loan behind
        if (dias < 0) {
            return false;
        }
        return true;
    }

    public ReturnLoanDTO returnLoan(LoansEntity loansEntity) {
        // first we calculate the costs
        ReturnLoanDTO loanCost = calculateCosts(loansEntity.getLoanId());
        System.out.println("1");
        List<String> toolslist = new ArrayList<>();
        loanCost.setLowDmgAmount(0L);
        loanCost.setTools(toolslist);
        // we have to update the tools state
        List<Long> toolsId = toolsLoansRepository.findByLoanId(loansEntity.getLoanId());
        for (Long toolId : toolsId) {
            ToolsEntity toolsEntity = toolsService.getToolsById(toolId);

            if ("Dañada".equals(toolsEntity.getInitialState())) {
                toolsEntity.setInitialState("Bueno");
            }else if("Malo".equals(toolsEntity.getInitialState())){
                toolsEntity.setInitialState("Bueno");
                List<String> tools = loanCost.getTools();
                tools.add(toolsEntity.getToolName());
                loanCost.setTools(tools);
                Long lowDmgAmount = loanCost.getLowDmgAmount();
                loanCost.setLowDmgAmount(lowDmgAmount+toolsEntity.getLowDmgFee());
            }
            toolsService.updateTool(toolsEntity);
        }
        //NOTE: the extra chargues only account for the repo ammount other costs
        //are held by fines
        loansEntity.setExtraCharges(loanCost.getRepoAmount()+loanCost.getFineAmount()+loanCost.getLowDmgAmount());

        // then create a record
        RecordsEntity record = new RecordsEntity();
        record.setRecordType("Return");
        record.setRecordAmount(loanCost.getRepoAmount()+loanCost.getFineAmount()+loanCost.getLowDmgAmount());
        LocalDateTime date = LocalDateTime.now();
        record.setRecordDate(Date.valueOf(date.toLocalDate()));
        record.setLoanId(loansEntity.getLoanId());
        record.setClientId(loansEntity.getClientId());
        recordsServices.saveRecord(record);

        // save loan
        //once is return the loan isnt active no more
        loansEntity.setActive(false);
        Optional<LoansEntity> returnLoan = saveLoan(loansEntity);
        loanCost.setLoan(returnLoan.get());
        return loanCost;
    }

    public ReturnLoanDTO calculateCosts(Long loanId) {
        ReturnLoanDTO dto = new ReturnLoanDTO();
        ReturnLoanDTO dto1 = new ReturnLoanDTO();
        ReturnLoanDTO dto2 = new ReturnLoanDTO();

        Optional<LoansEntity> loan = loansRepository.findById(loanId);
        if (reamaningDaysOnLoan(loanId) > 0) {
            dto2 = calculateRepoFine(loanId, loan.get().getClientId());
        }if (reamaningDaysOnLoan(loanId) < 0) {
            dto1 = calculateFine(loanId, loan.get().getClientId());
            dto2 = calculateRepoFine(loanId, loan.get().getClientId());
        }else{

        }
        dto.setRepoAmount(dto2.getRepoAmount());
        dto.setRepoFine(dto2.getRepoFine());
        dto.setFineAmount(dto1.getFineAmount());
        dto.setFine(dto1.getFine());
        return dto;
    }


    public ReturnLoanDTO calculateRepoFine(Long id, Long clientId){
        ReturnLoanDTO dto = new ReturnLoanDTO();
        List<Long> tools = toolsLoansRepository.findByLoanId(id);
        Long repofine = 0L;
        for(Long toolId : tools){
            if(toolsService.getToolsById(toolId).getInitialState().equals("Dañada")) {
                repofine += toolsService.getToolsById(toolId).getRepositionFee();
            }
        }

        if(repofine > 0){
            FineEntity newFine =  new FineEntity();
            newFine.setState("pendiente");
            newFine.setAmount(repofine);
            newFine.setType("dmg fine");
            newFine.setClientId(clientId);
            LocalDateTime date = LocalDateTime.now();
            newFine.setDate(Date.valueOf(date.toLocalDate()));
            newFine.setLoanId(id);
            fineService.saveFine(newFine);

            dto.setRepoAmount(repofine);
            dto.setRepoFine(newFine);
        }else {
            dto.setRepoAmount(0L);
        }
        return dto;
    }

    //verify that the loan ins't returned before its return date
    public Long reamaningDaysOnLoan(Long id){
        LoansEntity loan = loansRepository.findById(id).get();

        LocalDate date = LocalDate.now();

        //formating the return date
        String returnDate = loan.getReturnDate().toString();
        Date sqlDate2 = Date.valueOf(returnDate);
        LocalDate localDate2 = sqlDate2.toLocalDate();

        long dias = ChronoUnit.DAYS.between(date, localDate2);
        //if the differrence is negative it means the loan is late
        //therefore, the client has at least 1 day behind
        return dias;
    }



    //calculateFine
    public ReturnLoanDTO calculateFine(Long id, Long clientId){
        ReturnLoanDTO dto = new ReturnLoanDTO();
        if(reamaningDaysOnLoan(id)<0){
            //if its return after, them we calculate the fine
            //we need to get all the tools in the loan
            List<Long> tools = toolsLoansRepository.findByLoanId(id);
            Long fine = 0L;

            for(Long toolId : tools){
                //Multa atraso = días de atraso × tarifa diaria de multa.
                //per tool
                //multiply by -1 because if the loan is behind in days, those are going to be negative
                fine += -1 * reamaningDaysOnLoan(id) * toolsService.getToolsById(toolId).getDiaryFineFee();
            }

            if(fine > 0) {
                FineEntity newFine = new FineEntity();
                newFine.setState("pendiente");
                newFine.setAmount(fine);
                newFine.setType("behind fine");
                newFine.setClientId(clientId);
                LocalDateTime date = LocalDateTime.now();
                newFine.setDate(Date.valueOf(date.toLocalDate()));
                newFine.setLoanId(id);
                fineService.saveFine(newFine);

                dto.setFineAmount(fine);
                dto.setFine(newFine);
            }else{
                dto.setFineAmount(0L);
            }
        }

        return dto;
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
            List<Long> tools = toolsLoansService.getToolsIDsByLoanId(id);
            for(Long toolId : tools){
                toolsLoansRepository.deleteByToolId(toolId);
            }
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
