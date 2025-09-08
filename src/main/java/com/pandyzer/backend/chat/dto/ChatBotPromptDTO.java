// ChatBotPromptDTO.java
package com.pandyzer.backend.chat.dto;

public class ChatBotPromptDTO {

    private String prompt;
    // opcional: manter contexto entre mensagens no Flowise
    private String sessionId;

    public ChatBotPromptDTO() {}

    public ChatBotPromptDTO(String prompt) {
        this.prompt = prompt;
    }

    public ChatBotPromptDTO(String prompt, String sessionId) {
        this.prompt = prompt;
        this.sessionId = sessionId;
    }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
}
