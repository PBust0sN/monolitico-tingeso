package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientBehindEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClientBehindRepository extends JpaRepository<ClientBehindEntity, Long> {
    public ClientBehindEntity findByReportId(Long  reportId);
}
