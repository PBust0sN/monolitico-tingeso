package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsLoansEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsLoansRepository extends JpaRepository<ToolsLoansEntity, Long> {

    @Query(value = "SELECT tool_id FROM tools_loans WHERE loan_id = :loanId", nativeQuery = true)
    public List<Long> findByLoanId(@Param("loanId") Long loanId);
    @Query(value = "SELECT loan_id FROM tools_loans WHERE tool_id = :toolId", nativeQuery = true)
    public List<Long> findByToolId(Long toolId);

    @Transactional
    public void deleteByToolId(Long toolId);
}
