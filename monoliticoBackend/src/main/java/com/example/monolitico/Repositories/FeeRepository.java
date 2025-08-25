package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeRepository extends JpaRepository<FeeEntity, Long> {
}
