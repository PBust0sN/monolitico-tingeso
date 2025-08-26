package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findByRut(String rut);

    List<ClientEntity> findByMail(String mail);
}
