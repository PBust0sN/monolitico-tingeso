package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "STAFF")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long staffId;

    private String staffRut;
    private String staffName;
    private String staffMail;
    private String password;
    private Long roleId;
}
