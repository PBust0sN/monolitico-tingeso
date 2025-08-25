package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Toolsrepository extends JpaRepository<ToolsEntity,Long> {
}
