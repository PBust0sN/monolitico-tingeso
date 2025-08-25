package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "TOOLS")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ToolsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long tool_id;

    private String tool_name;
    private String initial_state;
    private String disponibility;
    private String category;
}
