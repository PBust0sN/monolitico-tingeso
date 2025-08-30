package com.example.monolitico.Repositories;

import com.example.monolitico.Entities.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity,Long> {
    public StaffEntity findByStaffRut(String staff_rut);
    public StaffEntity findByStaffMail(String mail);
}
