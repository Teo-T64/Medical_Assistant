package com.med.symptom.controller;

import com.med.symptom.dto.SymptomRequest;
import com.med.symptom.dto.SymptomResponse;

import com.med.symptom.service.SymptomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@AllArgsConstructor
public class SymptomController {

    private SymptomService symptomService;

    @PostMapping
    public ResponseEntity<SymptomResponse> trackSymptom(@RequestBody SymptomRequest request) {
        return ResponseEntity.ok(symptomService.trackSymptom(request));
    }

    @GetMapping
    public ResponseEntity<List<SymptomResponse>> getUserSymptoms(@RequestHeader("X-User-ID") Long userId) {
        return ResponseEntity.ok(symptomService.getUserSymptoms(userId));
    }

    @GetMapping("/{symptomId}")
    public ResponseEntity<SymptomResponse> getSymptomById(@PathVariable String symptomId) {
        return ResponseEntity.ok(symptomService.getSymptomById(symptomId));
    }
}
