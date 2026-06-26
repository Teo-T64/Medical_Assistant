package com.medassistant.aiservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Symptom {
    private String id;
    private Long userId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
