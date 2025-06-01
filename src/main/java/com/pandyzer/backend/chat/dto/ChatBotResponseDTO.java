package com.pandyzer.backend.chat.dto;

public class ChatBotResponseDTO {

    private String response;

    public ChatBotResponseDTO () {}

    public  ChatBotResponseDTO (String response) {

        this.response = response;

    }

    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

}
