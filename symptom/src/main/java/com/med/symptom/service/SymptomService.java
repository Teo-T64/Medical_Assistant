package com.med.symptom.service;

import com.med.symptom.dto.SymptomRequest;
import com.med.symptom.dto.SymptomResponse;
import com.med.symptom.model.Symptom;
import com.med.symptom.repository.SymptomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SymptomService {

    private final SymptomRepository symptomRepository;

    public SymptomResponse trackSymptom(SymptomRequest request) {
        Symptom symptom = Symptom.builder()
                .userId(request.getUserId())
                .type(request.getSymptomType())
                .description(request.getDescription())
                .build();
        Symptom savedSymptom = symptomRepository.save(symptom);
        return mapToResponse(savedSymptom);
    }

    private SymptomResponse mapToResponse(Symptom symptom) {
        SymptomResponse symptomResponse = new SymptomResponse();
        symptomResponse.setId(symptom.getId());
        symptomResponse.setUserId(symptom.getUserId());
        symptomResponse.setType(symptom.getType());
        symptomResponse.setDescription(symptom.getDescription());
        symptomResponse.setCreatedAt(symptom.getCreatedAt());
        symptomResponse.setUpdatedAt(symptom.getUpdatedAt());
        return symptomResponse;
    }
}
