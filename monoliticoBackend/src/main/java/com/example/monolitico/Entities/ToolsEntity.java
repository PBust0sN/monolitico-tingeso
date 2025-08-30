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
    private Long toolId;

    private String toolName;
    private String initialState;
    private String disponibility;
    private String category;
    private Long loanFee;
    private Long repostitionFee;
    private Long diaryFineFee;
}
