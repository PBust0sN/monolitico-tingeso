package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ToolsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ToolsRepository extends JpaRepository<ToolsEntity,Long> {
    public List<ToolsEntity> findByToolName(String tool_name);
    public List<ToolsEntity> findByDisponibility(String disponibility);
    public List<ToolsEntity> findByCategory(String category);

    //the stock is the amount of rows that have the field disponibility in 'Disponible'
    @Query(value = """
        SELECT COUNT(*) FROM tools
        WHERE tools.disponibility = 'Disponible' AND tools.tool_name = :tool_name
        """, nativeQuery = true)
    public Long findStockByToolName(String tool_name);

    @Query(value = "SELECT * FROM tools ORDER BY loan_count DESC LIMIT 10", nativeQuery = true)
    public List<ToolsEntity> findRanking();
}
