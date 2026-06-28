package com.medassistant.aiservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "suggestions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Suggestion {
    @Id
    private String id;
    private String symptomId;
    private Long userId;

    private String conditionName;
    private Double confidenceScore;
    private String conditionDescription;

    private List<String> lifestyleRecommendations;

    private List<Medicine> suggestedMedicines;

    private List<String> urgentCareWarnings;

    private String triageLevel;

    @CreatedDate
    private LocalDateTime createdAt;
}
