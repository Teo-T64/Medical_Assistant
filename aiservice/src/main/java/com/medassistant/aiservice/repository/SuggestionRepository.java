package com.medassistant.aiservice.repository;

import com.medassistant.aiservice.model.Suggestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends MongoRepository<Suggestion,String> {

    List<Suggestion> findByUserId(Long userId);

    Optional<Suggestion> findBySymptomId(String symptomId);
}
