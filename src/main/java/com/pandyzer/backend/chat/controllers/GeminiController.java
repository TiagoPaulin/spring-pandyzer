package com.pandyzer.backend.chat.controllers;

import com.pandyzer.backend.chat.dto.ChatBotPromptDTO;
import com.pandyzer.backend.chat.dto.ChatBotResponseDTO;
import com.pandyzer.backend.chat.services.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatbot")
public class GeminiController {

    @Autowired
    private GeminiService service;

    @PostMapping
    public ResponseEntity<ChatBotResponseDTO> getGeminiChatResponse(@RequestBody ChatBotPromptDTO request) {

        ChatBotResponseDTO obj = service.getGeminiResponse(request);
        return ResponseEntity.ok().body(obj);

    }

}
