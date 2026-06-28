package com.medassistant.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private String category;
    private List<String> activeIngredients;
    private String purpose;
    private List<String> precautions;
    private String dosageGuidance;
}