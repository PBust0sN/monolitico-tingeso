package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.LoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoansRepository extends JpaRepository<LoansEntity, Long> {
    public List<LoansEntity> findByUserResponsable(String responsable);

}
