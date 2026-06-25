package com.med.symptom.service;

import com.med.symptom.dto.SymptomRequest;
import com.med.symptom.dto.SymptomResponse;
import com.med.symptom.model.Symptom;
import com.med.symptom.repository.SymptomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymptomService {

    private final SymptomRepository symptomRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public SymptomResponse trackSymptom(SymptomRequest request) {
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid user " + request.getUserId());
        }
        Symptom symptom = Symptom.builder()
                .userId(request.getUserId())
                .type(request.getSymptomType())
                .description(request.getDescription())
                .build();
        Symptom savedSymptom = symptomRepository.save(symptom);

        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, savedSymptom);
        }catch(Exception e){
            log.error("Failed to publish to RabbitMQ: ",e);
        }

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

    public List<SymptomResponse> getUserSymptoms(Long userId) {
        List<Symptom> symptoms = symptomRepository.findAllByUserId(userId);
        return symptoms.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SymptomResponse getSymptomById(String symptomId) {
        return symptomRepository.findById(symptomId)
                .map(this::mapToResponse)
                .orElseThrow(()-> new RuntimeException("Symptom with" + symptomId + "does not exist"));

    }
}
