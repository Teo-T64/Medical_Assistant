package com.med.symptom.repository;

import com.med.symptom.model.Symptom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SymptomRepository extends MongoRepository<Symptom, String> {

    List<Symptom> findAllByUserId(Long userId);
}
