package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "CLIENT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long client_id;

    private Long client_rut;
    private String name;
    private String last_name;
    private String mail;
    private String phone_number;
    private String state;
    private String avaliable;
}
