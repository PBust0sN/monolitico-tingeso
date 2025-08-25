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
    private Long staff_id;

    private String staff_rut;
    private String staff_name;
    private String staff_mail;
}
