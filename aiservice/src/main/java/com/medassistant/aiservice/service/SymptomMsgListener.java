package com.medassistant.aiservice.service;

import com.medassistant.aiservice.model.Suggestion;
import com.medassistant.aiservice.model.Symptom;
import com.medassistant.aiservice.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymptomMsgListener {
    private final SymptomAIService symptomAIService;
    private final SuggestionRepository suggestionRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processSymptom(Symptom symptom) {
        log.info("Received Symptom for processing: {}", symptom.getId());
        Suggestion suggestion = symptomAIService.generateSuggestion(symptom);
        suggestionRepository.save(suggestion);

    }
}
