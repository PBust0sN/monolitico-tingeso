package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ReportsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReportsRepository extends CrudRepository<ReportsEntity, Long> {

    public List<ReportsEntity> findByReportDateBetween(Date date1, Date date2);
}
