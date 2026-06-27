package com.medassistant.aiservice.service;

import com.medassistant.aiservice.model.Suggestion;
import com.medassistant.aiservice.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;

    public List<Suggestion> getUserSuggestion(Long userId) {
        return suggestionRepository.findByUserId(userId);
    }

    public Suggestion getSymptomSuggestion(String symptomId) {
        return suggestionRepository.findBySymptomId(symptomId)
                .orElseThrow(()-> new RuntimeException("No suggestion found for symptomId: " + symptomId));
    }
}
