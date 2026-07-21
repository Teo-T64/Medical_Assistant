package com.med.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId) {
        return userServiceWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(WebClientResponseException.class, ex ->
                        log.error("Validation failed for userId {}. Status: {}, Body: {}",
                                userId, ex.getStatusCode(), ex.getResponseBodyAsString())
                )
                .onErrorResume(WebClientResponseException.class, ex -> Mono.just(false));
    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
        return userServiceWebClient.post()
                .uri("/api/users/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    HttpStatus status = (HttpStatus) ex.getStatusCode();

                    if (status.is5xxServerError()) {
                        return Mono.error(new RuntimeException(
                                "User management service is currently unavailable. Please try again later.", ex
                        ));
                    }

                    return Mono.error(new IllegalArgumentException("Registration failed: " + ex.getResponseBodyAsString()));
                });
    }
}