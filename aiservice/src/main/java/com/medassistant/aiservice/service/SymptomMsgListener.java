package com.medassistant.aiservice.service;

import com.medassistant.aiservice.model.Symptom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymptomMsgListener {
    private final SymptomAIService symptomAIService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processSymptom(Symptom symptom) {
        log.info("Received Symptom for processing: {}", symptom.getId());
        log.info("Generated Suggestion: {}", symptomAIService.generateSuggestion(symptom));

    }
}
