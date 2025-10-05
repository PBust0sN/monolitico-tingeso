package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsLoanReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsLoanReportRepository extends JpaRepository<ToolsLoanReportEntity,Long> {
    public List<Long> findByLoanId(Long toolId);
}
