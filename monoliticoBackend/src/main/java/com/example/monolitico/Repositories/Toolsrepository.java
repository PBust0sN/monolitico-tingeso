package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Toolsrepository extends JpaRepository<ToolsEntity,Long> {
    public List<ToolsEntity> findByTool_name(String tool_name);
    public List<ToolsEntity> findByDisponibility(String disponibility);
    public List<ToolsEntity> findByCategory(String category);
}
