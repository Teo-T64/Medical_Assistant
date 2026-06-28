package com.medassistant.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medassistant.aiservice.model.Medicine;
import com.medassistant.aiservice.model.Suggestion;
import com.medassistant.aiservice.model.Symptom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SymptomAIService {
    private final GeminiService geminiService;

    public Suggestion generateSuggestion(Symptom symptom) {
        String prompt = createPromptForSymptom(symptom);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {} ", aiResponse);
        return processAiResponse(symptom, aiResponse);
    }

    private Suggestion processAiResponse(Symptom symptom, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            JsonNode analysisJson = mapper.readTree(jsonContent);

            JsonNode conditionNode = analysisJson.path("predicted_condition");
            String triageLevel = analysisJson.path("triage_level").asText("Moderate");

            List<String> lifestyle = extractSimpleList(analysisJson.path("treatment_recommendations"));
            List<String> urgentCare = extractSimpleList(analysisJson.path("when_to_seek_urgent_care"));
            List<Medicine> medicines = extractMedicines(analysisJson.path("suggested_medicines"));

            return Suggestion.builder()
                    .symptomId(symptom.getId())
                    .userId(symptom.getUserId())
                    .conditionName(conditionNode.path("name").asText("Unknown Condition"))
                    .confidenceScore(conditionNode.path("confidence_percentage").asDouble(0.0))
                    .conditionDescription(conditionNode.path("description").asText("No description available"))
                    .lifestyleRecommendations(lifestyle)
                    .suggestedMedicines(medicines)
                    .urgentCareWarnings(urgentCare)
                    .triageLevel(triageLevel)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultSuggestion(symptom);
        }
    }

    private Suggestion createDefaultSuggestion(Symptom symptom) {
        return Suggestion.builder()
                .symptomId(symptom.getId())
                .userId(symptom.getUserId())
                .conditionName("General Assessment Needed")
                .confidenceScore(0.0)
                .conditionDescription("Unable to generate detailed medical analysis automatically.")
                .lifestyleRecommendations(Collections.singletonList("Rest and stay well-hydrated."))
                .suggestedMedicines(Collections.emptyList())
                .urgentCareWarnings(Arrays.asList(
                        "If symptoms rapidly worsen, seek immediate help.",
                        "Go to the nearest emergency room if you experience severe shortness of breath or persistent chest pain."
                ))
                .triageLevel("Moderate")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSimpleList(JsonNode node) {
        List<String> list = new ArrayList<>();
        if (node.isArray()) {
            node.forEach(item -> list.add(item.asText()));
        }
        return list;
    }

    private List<Medicine> extractMedicines(JsonNode medicinesNode) {
        List<Medicine> medicines = new ArrayList<>();
        if (medicinesNode.isArray()) {
            medicinesNode.forEach(medNode -> {
                List<String> ingredients = new ArrayList<>();
                medNode.path("active_ingredients").forEach(ing -> ingredients.add(ing.asText()));

                List<String> precautions = new ArrayList<>();
                medNode.path("precautions").forEach(prec -> precautions.add(prec.asText()));

                Medicine medicine = Medicine.builder()
                        .category(medNode.path("category").asText())
                        .activeIngredients(ingredients)
                        .purpose(medNode.path("purpose").asText())
                        .precautions(precautions)
                        .dosageGuidance("Follow label instructions or consult a pharmacist.")
                        .build();

                medicines.add(medicine);
            });
        }
        return medicines;
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
              "precautions": ["Take with food", "Do not mix with alcohol"]
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