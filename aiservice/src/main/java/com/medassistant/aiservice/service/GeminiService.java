package com.medassistant.aiservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiUrl;

    @Value("${gemini.api.key}")
    private String geminiKey;

    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", question)
                })
        });

        try {
            return webClient.post()
                    .uri(geminiUrl)
                    .header("x-goog-api-key", geminiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (WebClientResponseException e) {
            log.error("Google API rejected the request!");
            log.error("HTTP Status Code: {}", e.getStatusCode());
            log.error("Google Error Body: {}", e.getResponseBodyAsString());

            throw e;
        } catch (Exception e) {
            log.error("A non-HTTP error occurred: ", e);
            throw e;
        }
    }
}