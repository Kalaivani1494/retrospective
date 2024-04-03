package com.example.retrospective.service;

import com.example.retrospective.CustomExceptionHandler;
import com.example.retrospective.model.Feedback;
import com.example.retrospective.model.Retrospective;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RetrospectiveServiceInterface {
    ResponseEntity<?> createRetrospective(Retrospective retrospective);

    ResponseEntity<?> addFeedbackItem(String name, Feedback feedback) throws CustomExceptionHandler.ParticipantNotAssociatedException;

    Retrospective getRetrospectiveByName(String name);

    boolean isParticipantInRetrospective(String retrospectiveName, String participantName);

    Retrospective updateFeedbackItem(String name, Feedback feedback);

    List<Retrospective> getAllRetrospectives(int page, int pageSize);

    List<Retrospective> searchRetrospectivesByDate(String date, int page, int pageSize);
}