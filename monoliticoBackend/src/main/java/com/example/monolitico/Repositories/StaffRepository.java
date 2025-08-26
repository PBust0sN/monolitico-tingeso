package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<StaffEntity,Long> {
    public StaffEntity findByStaff_rut(String staff_rut);
    public StaffEntity findByMail(String mail);
}
