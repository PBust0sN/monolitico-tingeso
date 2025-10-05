package com.example.monolitico.Service;

import com.example.monolitico.Entities.ToolsLoanReportEntity;
import com.example.monolitico.Repositories.ToolsLoanReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolsLoanReportService {

    @Autowired
    private ToolsLoanReportRepository toolsLoanReportRepository;

    public ToolsLoanReportEntity  createToolsLoanReport(ToolsLoanReportEntity toolsLoanReportEntity){
        return toolsLoanReportRepository.save(toolsLoanReportEntity);
    }

    public List<Long> findByLoanId(Long id){
        return toolsLoanReportRepository.findByLoanId(id);
    }
}
