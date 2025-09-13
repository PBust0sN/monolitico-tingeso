package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.RecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface RecordsRepository extends JpaRepository<RecordsEntity, Long> {
    public List<RecordsEntity> findByRecordDateBetween(Date start_date, Date end_date);
    public List<RecordsEntity> findByRecordType(String record_type);
}
