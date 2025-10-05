package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientEntity;
import com.example.monolitico.Entities.LoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findByRut(String client_rut);

    List<ClientEntity> findByMail(String mail);

    @Query(value =
        """
        SELECT loans.* 
        FROM client INNER JOIN client_loans
        ON client.client_id = client_loans.client_id
                INNER JOIN loans
                ON client_loans.loan_id = loans.loan_id
        WHERE client.client_id = :id
        """, nativeQuery = true)
    public List<LoansEntity> getAllLoansByClientId(@Param("id") Long id);

    public Long findIdByRut(String client_name);
}
