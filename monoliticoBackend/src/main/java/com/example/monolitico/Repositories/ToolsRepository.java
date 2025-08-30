package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolsRepository extends JpaRepository<ToolsEntity,Long> {
    public List<ToolsEntity> findByToolName(String tool_name);
    public List<ToolsEntity> findByDisponibility(String disponibility);
    public List<ToolsEntity> findByCategory(String category);
}
