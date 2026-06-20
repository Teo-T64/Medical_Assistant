package com.med.symptom.dto;

import com.med.symptom.model.Symptom;
import com.med.symptom.model.SymptomType;
import lombok.Data;

@Data
public class SymptomRequest {
    private Long userId;
    private SymptomType symptomType;
    private String description;

}
