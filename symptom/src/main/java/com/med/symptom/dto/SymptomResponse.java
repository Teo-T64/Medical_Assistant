package com.med.symptom.dto;

import com.med.symptom.model.SymptomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
public class SymptomResponse {
    private String id;
    private Long userId;
    private SymptomType type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
