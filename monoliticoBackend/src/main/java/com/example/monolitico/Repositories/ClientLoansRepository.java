package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientLoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientLoansRepository extends JpaRepository<ClientLoansEntity, Long> {

    public List<ClientLoansEntity> findByLoanId(Long loanId);
    public List<ClientLoansEntity> findByClientId(Long clientId);
}
