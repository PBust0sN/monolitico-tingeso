package com.example.monolitico.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("tool_name")
    private String toolName;
    @JsonProperty("initial_state")
    private String initialState;
    @JsonProperty("disponibility")
    private String disponibility;
    @JsonProperty("category")
    private String category;
    @JsonProperty("loan_fee")
    private Long loanFee;
    @JsonProperty("reposition_fee")
    private Long repositionFee;
    @JsonProperty("diary_fine_fee")
    private Long diaryFineFee;
    @JsonProperty("stock")
    private Long stock;
    @JsonProperty("loan_count")
    private Long loanCount;
    @JsonProperty("low_dmg_fee")
    private Long lowDmgFee;
}
