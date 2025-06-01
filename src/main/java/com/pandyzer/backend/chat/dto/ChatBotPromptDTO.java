package com.pandyzer.backend.chat.dto;

public class ChatBotPromptDTO {

    private String prompt;

    public ChatBotPromptDTO () {}

    public  ChatBotPromptDTO (String prompt) {

        this.prompt = prompt;

    }

    public String getPrompt() {
        return prompt;
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

}
