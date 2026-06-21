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
    private String userId;
    private String symptomType;
    private String suggestion;
    private List<String> improvements;
    private List<String> safety;
    @CreatedDate
    private LocalDateTime createdAt;

}
