package com.med.symptom.controller;

import com.med.symptom.dto.SymptomRequest;
import com.med.symptom.dto.SymptomResponse;
import com.med.symptom.model.Symptom;

import com.med.symptom.service.SymptomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/symptoms")
@AllArgsConstructor
public class SymptomController {

    private SymptomService symptomService;

    @PostMapping
    public ResponseEntity<SymptomResponse> trackSymptom(@RequestBody SymptomRequest request) {
        return ResponseEntity.ok(symptomService.trackSymptom(request));
    }
}
