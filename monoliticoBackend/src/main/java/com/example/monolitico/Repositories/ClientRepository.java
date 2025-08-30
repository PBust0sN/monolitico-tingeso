package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    public ClientEntity findByRut(String client_rut);

    List<ClientEntity> findByMail(String mail);
}
