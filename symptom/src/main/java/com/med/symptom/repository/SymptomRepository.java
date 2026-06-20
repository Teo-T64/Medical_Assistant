package com.med.symptom.repository;

import com.med.symptom.model.Symptom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SymptomRepository extends MongoRepository<Symptom, String> {

}
