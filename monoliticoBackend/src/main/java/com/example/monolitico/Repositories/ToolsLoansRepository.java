package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsLoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsLoansRepository extends JpaRepository<ToolsLoansEntity, Long> {

    public List<Long> findByLoanId(Long loanId);
    public List<Long> findByToolId(Long toolId);
}
