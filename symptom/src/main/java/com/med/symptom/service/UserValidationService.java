package com.med.symptom.service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public boolean validateUser(Long userId) {
        try{
            return userServiceWebClient.get()
                    .uri("/api/users/{userId}/validate",userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        }catch(WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
            }
        }
        return false;
    }
}
