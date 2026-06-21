package com.medassistant.aiservice.controller;

import com.medassistant.aiservice.model.Suggestion;
import com.medassistant.aiservice.repository.SuggestionRepository;
import com.medassistant.aiservice.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suggestion")
public class SuggestionController {
    private final SuggestionService  suggestionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Suggestion>> getUserSuggestion(@PathVariable Long userId) {
        return ResponseEntity.ok(suggestionService.getUserSuggestion(userId));
    }
    @GetMapping("/symptom/{symptomId}")
    public ResponseEntity<Suggestion> getSymptomSuggestion(@PathVariable String symptomId) {
        return ResponseEntity.ok(suggestionService.getSymptomSuggestion(symptomId));
    }
}
