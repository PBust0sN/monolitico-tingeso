package com.example.monolitico.Services;

import com.example.monolitico.Entities.StaffEntity;
import com.example.monolitico.Repositories.StaffRepository;
import com.example.monolitico.Service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    private StaffEntity staff;

    @BeforeEach
    void setUp() {
        staff = new StaffEntity();
        staff.setStaffId(1L);
        staff.setStaffRut("12345678-9");
        staff.setStaffName("John Doe");
        staff.setStaffMail("john@example.com");
        staff.setPassword("password123");
    }

    @Test
    void testGetAllStaff() {
        List<StaffEntity> staffList = List.of(staff);
        when(staffRepository.findAll()).thenReturn(staffList);

        List<StaffEntity> result = staffService.getAllStaff();

        assertEquals(1, result.size());
        assertEquals(staff, result.get(0));
        verify(staffRepository, times(1)).findAll();
    }

    @Test
    void testSaveStaff() {
        when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

        StaffEntity saved = staffService.saveStaff(staff);

        assertEquals(staff, saved);
        verify(staffRepository, times(1)).save(staff);
    }

    @Test
    void testGetStaffById() {
        when(staffRepository.findById(1L)).thenReturn(Optional.of(staff));

        StaffEntity result = staffService.getStaffById(1L);

        assertEquals(staff, result);
        verify(staffRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateStaff() {
        when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

        StaffEntity updated = staffService.updateStaff(staff);

        assertEquals(staff, updated);
        verify(staffRepository, times(1)).save(staff);
    }

    @Test
    void testDeleteStaffSuccess() throws Exception {
        doNothing().when(staffRepository).deleteById(1L);

        boolean result = staffService.deleteStaff(1L);

        assertTrue(result);
        verify(staffRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStaffThrowsException() {
        doThrow(new RuntimeException("DB error")).when(staffRepository).deleteById(1L);

        Exception exception = assertThrows(Exception.class, () -> staffService.deleteStaff(1L));

        assertEquals("DB error", exception.getMessage());
        verify(staffRepository, times(1)).deleteById(1L);
    }
}
