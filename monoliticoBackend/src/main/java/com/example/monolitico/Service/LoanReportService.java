package com.example.monolitico.Service;

import com.example.monolitico.Entities.LoansReportEntity;
import com.example.monolitico.Repositories.LoanReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanReportService {
    @Autowired
    private LoanReportRepository loanReportRepository;

    public List<LoansReportEntity> getAllLoansReport(){
        return loanReportRepository.findAll();
    }

    public List<LoansReportEntity> getLoansReportByReportId(Long reportId){
        return loanReportRepository.findByReportId(reportId);
    }

    public LoansReportEntity createLoansReport(LoansReportEntity loansReportEntity){
        return loanReportRepository.save(loansReportEntity);
    }

    public LoansReportEntity updateLoansReport(LoansReportEntity loansReportEntity){
        return loanReportRepository.save(loansReportEntity);
    }

    public boolean deleteLoansReport(LoansReportEntity loansReportEntity) throws Exception{
        try{
            loanReportRepository.delete(loansReportEntity);
            return true;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
