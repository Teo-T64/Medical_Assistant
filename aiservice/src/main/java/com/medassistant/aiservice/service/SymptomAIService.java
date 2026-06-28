package com.medassistant.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medassistant.aiservice.model.Suggestion;
import com.medassistant.aiservice.model.Symptom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymptomAIService {
    private final GeminiService geminiService;

    public String generateSuggestion(Symptom symptom) {
        String prompt = createPromptForSymptom(symptom);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("AI Response is {}", aiResponse);
        processAiResponse(symptom, aiResponse);
        return aiResponse;
    }

    private void processAiResponse(Symptom symptom, String aiResponse) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");
            String jsonContent = textNode.asText().replace("```json\\n","").replace("\\n```","").trim();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private String createPromptForSymptom(Symptom symptom) {
        return String.format("""
        Analyze the following patient-reported symptom description:
        
        "%s"
        
        Extract the core clinical symptoms from their text and provide detailed recommendations.
        Respond EXACTLY in the following JSON format without any markdown formatting, backticks, or extra text:
        {
          "extracted_symptoms": [
            "Symptom 1",
            "Symptom 2"
          ],
          "predicted_condition": {
            "name": "Name of the most likely condition",
            "confidence_percentage": 0.0,
            "description": "Brief medical description of the condition"
          },
          "differential_diagnoses": [
            "Alternative condition 1",
            "Alternative condition 2"
          ],
          "triage_level": "Low | Moderate | High | Emergency",
          "treatment_recommendations": [
            "Actionable home remedy 1",
            "Actionable home remedy 2"
          ],
          "suggested_medicines": [
            {
              "category": "e.g., Analgesic",
              "active_ingredients": ["Ibuprofen"],
              "purpose": "What it treats",
              "precautions": "Standard warning"
            }
          ],
          "when_to_seek_urgent_care": [
            "Red flag to watch for 1"
          ],
          "disclaimer": "This is an AI-generated analysis and does not constitute professional medical advice, diagnosis, or treatment."
        }
        """, symptom.getDescription());
    }
}
