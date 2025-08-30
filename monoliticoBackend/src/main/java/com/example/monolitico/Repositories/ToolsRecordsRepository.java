package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsRecordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsRecordsRepository extends JpaRepository<ToolsRecordsEntity,Long> {

    public List<ToolsRecordsEntity> findByToolId(Long toolId);
    public List<ToolsRecordsEntity> findByRecordIdId(Long recordId);
}
