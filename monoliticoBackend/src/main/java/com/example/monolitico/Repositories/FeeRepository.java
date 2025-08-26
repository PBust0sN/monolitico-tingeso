package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeeRepository extends JpaRepository<FeeEntity, Long> {
    public List<FeeEntity> findByFee_type(String fee_type);

}
