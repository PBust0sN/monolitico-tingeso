package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.ClientLoansEntity;
import com.example.monolitico.Entities.LoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientLoansRepository extends JpaRepository<ClientLoansEntity, Long> {

    public List<Long> findByLoanId(Long loanId);
    public List<Long> findByClientId(Long clientId);
}
