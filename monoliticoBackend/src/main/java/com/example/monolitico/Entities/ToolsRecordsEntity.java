package com.example.monolitico.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "TOOLS_RECORDS")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ToolsRecordsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToolsRecords;

    private Long toolId;
    private Long recordId;
}
