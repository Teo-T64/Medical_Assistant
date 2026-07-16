package com.assistant.service.repository;

import com.assistant.service.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Boolean existsByKeycloakId(String keycloakId);

    User findByEmail(String email);
}
