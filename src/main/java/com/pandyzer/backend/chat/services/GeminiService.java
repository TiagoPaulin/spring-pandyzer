package com.pandyzer.backend.chat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pandyzer.backend.chat.dto.ChatBotResponseDTO;
import com.pandyzer.backend.chat.models.Content;
import com.pandyzer.backend.chat.models.GeminiRequest;
import com.pandyzer.backend.chat.models.GeminiResponse;
import com.pandyzer.backend.chat.models.Part;
import com.pandyzer.backend.chat.services.exceptions.GeminiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    private final RestTemplate restTemplate;
    private static final String GEMINI_API_PATH = "v1beta/models/gemini-2.5-flash-preview-05-20:generateContent";

    public GeminiService() {
        this.restTemplate = new RestTemplate();
    }

    public ChatBotResponseDTO getGeminiResponse(String userPrompt) {

        Part userPart = new Part(userPrompt);
        Content userContent = new Content("user", Collections.singletonList(userPart));
        GeminiRequest request = new GeminiRequest(Collections.singletonList(userContent));
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        try {

            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(request);
            System.out.println("JSON da Requisição Gemini: " + jsonRequest);

        } catch (Exception e) {

            throw new GeminiException("Erro ao serializar JSON da requisição (RestTemplate): " + e.getMessage());

        }

        try {

            String fullUrl = geminiApiUrl + GEMINI_API_PATH + "?key=" + geminiApiKey;
            System.out.println("Tentando chamar Gemini API (RestTemplate) em: " + fullUrl);

            ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(
                    fullUrl,
                    entity,
                    GeminiResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                GeminiResponse geminiResponse = response.getBody();
                if (geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                    Content modelContent = geminiResponse.getCandidates().get(0).getContent();
                    if (modelContent != null && modelContent.getParts() != null && !modelContent.getParts().isEmpty()) {
                        String geminiTextResponse = modelContent.getParts().stream()
                                .map(Part::getText)
                                .collect(Collectors.joining(" "));
                        return new ChatBotResponseDTO(geminiTextResponse);
                    }
                }
            }

            throw new GeminiException("Não foi possível obter uma resposta do Gemini (RestTemplate). Resposta inesperada ou vazia.");

        } catch (HttpClientErrorException e) {

            throw new GeminiException("Erro na API Gemini: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

        } catch (ResourceAccessException e) {

            throw new GeminiException("Ocorreu um erro de conexão ao processar sua solicitação com o Gemini: " + e.getMessage());

        } catch (Exception e) {

            throw new GeminiException("Ocorreu um erro inesperado ao processar sua solicitação com o Gemini.");

        }

    }

}
