package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, Long> {
    public List<FeeEntity> findByFeeType(String type);

}
