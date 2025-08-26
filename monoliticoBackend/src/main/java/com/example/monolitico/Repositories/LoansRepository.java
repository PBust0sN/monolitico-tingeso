package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.LoansEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoansRepository extends JpaRepository<LoansEntity, Long> {
    public List<LoansEntity> findByUser_responsable(String responsable);

}
