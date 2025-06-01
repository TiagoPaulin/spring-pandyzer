package com.pandyzer.backend.chat.models;

import java.util.List;

public class Candidate {

    private Content content;
    private String finishReason;
    private List<SafetyRating> safetyRatings;

    public Candidate () {}

    public Candidate (Content content, String finishReason, List<SafetyRating> safetyRatings) {

        this.content = content;
        this.finishReason = finishReason;
        this.safetyRatings = safetyRatings;

    }

    public Content getContent() {
        return content;
    }
    public String getFinishReason() {
        return finishReason;
    }
    public List<SafetyRating> getSafetyRatings() {
        return safetyRatings;
    }

    public void setContent(Content content) {
        this.content = content;
    }
    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
    public void setSafetyRatings(List<SafetyRating> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }

}
