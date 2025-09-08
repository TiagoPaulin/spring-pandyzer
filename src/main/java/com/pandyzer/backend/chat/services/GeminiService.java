package com.pandyzer.backend.chat.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pandyzer.backend.chat.dto.ChatBotPromptDTO;
import com.pandyzer.backend.chat.dto.ChatBotResponseDTO;
import com.pandyzer.backend.chat.services.exceptions.GeminiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class GeminiService {

    @Value("${flowise.base-url:http://localhost:3000}")
    private String baseUrl;

    @Value("${flowise.chatflow-id}")
    private String chatflowId;

    @Value("${flowise.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // usa só Spring (sem Apache HttpClient)
    public GeminiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ChatBotResponseDTO getGeminiResponse(ChatBotPromptDTO promptDTO) {
        if (chatflowId == null || chatflowId.isBlank()) {
            throw new GeminiException("flowise.chatflow-id não configurado");
        }
        if (promptDTO == null || !StringUtils.hasText(promptDTO.getPrompt())) {
            throw new GeminiException("Prompt vazio");
        }

        final String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/v1/prediction/")
                .path(chatflowId) // /prediction (singular)
                .toUriString();

        Map<String, Object> body = new HashMap<>();
        body.put("question", promptDTO.getPrompt());

        String sessionId = StringUtils.hasText(promptDTO.getSessionId())
                ? promptDTO.getSessionId()
                : UUID.randomUUID().toString();

        Map<String, Object> override = new HashMap<>();
        override.put("sessionId", sessionId);
        body.put("overrideConfig", override);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasText(apiKey)) {
            headers.set("Authorization", "Bearer " + apiKey);
        }

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new GeminiException("Flowise retornou HTTP " + response.getStatusCodeValue());
            }

            String respBody = response.getBody();
            if (!StringUtils.hasText(respBody)) {
                throw new GeminiException("Resposta vazia do Flowise");
            }

            String text = extractText(respBody);
            return new ChatBotResponseDTO(text);

        } catch (HttpStatusCodeException httpEx) {
            String details = safeExtractMessage(httpEx.getResponseBodyAsString());
            throw new GeminiException("Falha HTTP " + httpEx.getStatusCode().value() + " do Flowise: " + details);
        } catch (Exception e) {
            throw new GeminiException("Falha ao consultar Flowise: " + e.getMessage());
        }
    }

    private String extractText(String respBody) throws Exception {
        JsonNode root = objectMapper.readTree(respBody);
        String text = root.path("text").asText();
        if (!StringUtils.hasText(text)) text = root.path("message").asText();
        if (!StringUtils.hasText(text)) text = root.path("output").asText();
        if (!StringUtils.hasText(text)) text = respBody; // fallback bruto p/ debug
        return text;
    }

    private String safeExtractMessage(String body) {
        try {
            if (!StringUtils.hasText(body)) return "(sem corpo)";
            JsonNode node = objectMapper.readTree(body);
            String msg = node.path("message").asText();
            if (StringUtils.hasText(msg)) return msg;
            msg = node.path("error").asText();
            return StringUtils.hasText(msg) ? msg : body;
        } catch (Exception e) {
            return body;
        }
    }
}
