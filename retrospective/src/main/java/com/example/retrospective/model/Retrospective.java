package com.example.retrospective.model;

import java.util.ArrayList;
import java.util.List;

public class Retrospective {
    private String name;
    private String summary;
    private String date;
    private List<String> participants;
    private List<Feedback> feedbackItems;

    // Constructor, getters, and setters
    // Omitted for brevity
    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public void setFeedback(List<Feedback> feedbackItems) {
        this.feedbackItems = feedbackItems;
    }

    public void addFeedbackItem(Feedback feedback) {
        if (feedbackItems == null) {
            feedbackItems = new ArrayList<>();
        }
        feedbackItems.add(feedback);
    }

    public void updateFeedbackItem(String name, Feedback feedback) {
        if (feedbackItems != null && name != null) {
            for (int i = 0; i < feedbackItems.size(); i++) {
                Feedback currentFeedback = feedbackItems.get(i);
                if (currentFeedback != null && currentFeedback.getName().equals(name)) {
                    feedbackItems.set(i, feedback);
                }
            }
        }
    }

    public String getDate() {
        return date;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public String getName() {
        return name;
    }

    public List<Feedback> getFeedbackItems() {
        return feedbackItems;
    }

}
