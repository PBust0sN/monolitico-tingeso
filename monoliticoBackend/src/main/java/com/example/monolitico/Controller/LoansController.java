package com.example.monolitico.Controller;

import com.example.monolitico.DTO.ReturnLoanDTO;
import com.example.monolitico.DTO.NewLoanDTO;
import com.example.monolitico.Entities.LoansEntity;
import com.example.monolitico.Service.LoansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class LoansController {
    @Autowired
    LoansService loansService;

    @GetMapping("/")
    public ResponseEntity<List<LoansEntity>> getAllLoans(){
        List<LoansEntity> loans = loansService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoansEntity> getLoanById(@PathVariable Long id){
        LoansEntity loan = loansService.findLoanById(id);
        return ResponseEntity.ok(loan);
    }

    @PostMapping("/")
    public ResponseEntity<Optional<LoansEntity>> saveLoan(@RequestBody LoansEntity loan){
        Optional<LoansEntity> loanEntity = loansService.saveLoan(loan);
        return ResponseEntity.ok(loanEntity);
    }

    @PutMapping("/")
    public ResponseEntity<LoansEntity> updateLoan(@RequestBody LoansEntity loan){
        LoansEntity newLoan = loansService.updateLoan(loan);
        return ResponseEntity.ok(newLoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LoansEntity> deleteLoan(@PathVariable Long id) throws  Exception{
        var isDeleted = loansService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/new")
    public ResponseEntity<List<String>> saveNewLoan(@RequestBody NewLoanDTO newLoan){
        List<String> errors = loansService.AddLoan(
                newLoan.getStaff_id(),
                newLoan.getClient_id(),
                newLoan.getTools_id(),
                newLoan.getDays()
        );
        return ResponseEntity.ok(errors);
    }

    @PostMapping("/return")
    public ResponseEntity<ReturnLoanDTO> returnLoan(@RequestBody LoansEntity loan){
        ReturnLoanDTO returN = loansService.returnLoan(loan);
        return ResponseEntity.ok(returN);
    }

    @GetMapping("/calculate/cost/{id}")
    public ResponseEntity<ReturnLoanDTO> calculateLoanCost(@PathVariable Long id){
        ReturnLoanDTO cost = loansService.calculateCosts(id);
        return ResponseEntity.ok(cost);
    }
    @PostMapping("/checkdates")
    public ResponseEntity<Boolean> checkDate(@RequestBody LoansEntity loansEntity){
        boolean bool = loansService.checkDates(loansEntity);
        return  ResponseEntity.ok(bool);
    }

}

