package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.RecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReordsRepository extends JpaRepository<RecordsEntity, Long> {
}
