package com.example.monolitico.Repository;

import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Repositories.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StaffRepositoryTest {

    @Autowired
    private StaffRepository staffRepository;

    private StaffEntity staff;

    @BeforeEach
    void setUp() {
        staff = new StaffEntity();
        staff.setStaffRut("12345678-9");
        staff.setStaffName("Juan Perez");
        staff.setStaffMail("juan.perez@test.com");
        staff.setPassword("password123");
    }

    @Test
    void testSaveStaff() {
        StaffEntity saved = staffRepository.save(staff);
        assertNotNull(saved.getStaffId());
        assertEquals("Juan Perez", saved.getStaffName());
    }

    @Test
    void testFindById() {
        StaffEntity saved = staffRepository.save(staff);
        Optional<StaffEntity> found = staffRepository.findById(saved.getStaffId());
        assertTrue(found.isPresent());
        assertEquals(saved.getStaffId(), found.get().getStaffId());
    }

    @Test
    void testFindAll() {
        staffRepository.save(staff);
        List<StaffEntity> all = staffRepository.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteById() {
        StaffEntity saved = staffRepository.save(staff);
        staffRepository.deleteById(saved.getStaffId());
        Optional<StaffEntity> deleted = staffRepository.findById(saved.getStaffId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindByStaffRut() {
        staffRepository.save(staff);
        StaffEntity found = staffRepository.findByStaffRut("12345678-9");
        assertNotNull(found);
        assertEquals("Juan Perez", found.getStaffName());
    }

    @Test
    void testFindByStaffMail() {
        staffRepository.save(staff);
        StaffEntity found = staffRepository.findByStaffMail("juan.perez@test.com");
        assertNotNull(found);
        assertEquals("12345678-9", found.getStaffRut());
    }
}
