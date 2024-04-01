package com.example.retrospective.service;
import com.example.retrospective.CustomExceptionHandler;
import com.example.retrospective.controller.RetrospectiveController;
import com.example.retrospective.model.Feedback;
import com.example.retrospective.model.Retrospective;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RetrospectiveService {

    private final Map<String, Retrospective> retrospectives = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RetrospectiveController.class);
    public Retrospective createRetrospective(Retrospective retrospective) {
        if (retrospective.getDate() == null) {
            logger.error("Retrospective cannot be created without a date.");
            throw new IllegalArgumentException("Retrospective cannot be created without a date.");
        }
        if (retrospective.getParticipants() == null || retrospective.getParticipants().isEmpty()) {
            logger.error("Retrospective cannot be created without participants.");
            throw new IllegalArgumentException("Retrospective cannot be created without participants.");
        }
        String name = retrospective.getName();
        if (retrospectives.containsKey(name)) {
            logger.error("Retrospective with name {} already exists.", name);
            throw new IllegalArgumentException(STR."Retrospective with name \{name} already exists.");
        }
        retrospectives.put(name, retrospective);
        logger.info("Retrospective created: {}", retrospective);
        return retrospective;
    }

    public ResponseEntity<?> addFeedbackItem(String name, Feedback feedback) throws CustomExceptionHandler.ParticipantNotAssociatedException {
        try {
            Retrospective retrospective = getRetrospectiveByName(name);
            boolean isParticipantInRetrospective = isParticipantInRetrospective(name, feedback.getName());
           if (retrospective == null) {
               logger.error("Retrospective with name {} does not exist.", name);
               throw new IllegalArgumentException(STR."Retrospective with name \{name} does not exist.");
           }

           if (!isParticipantInRetrospective) {
               logger.error("Participant {} is not associated with the retrospective.", feedback.getName());
               throw new CustomExceptionHandler.ParticipantNotAssociatedException(STR."Participant \{feedback.getName()} is not associated with the retrospective.");
           }


           retrospective.addFeedbackItem(feedback);
           logger.info("Feedback item added to retrospective: {}", retrospective);
           return ResponseEntity.ok(getRetrospectiveByName(name));
    }   catch (CustomExceptionHandler.ParticipantNotAssociatedException e) {
            String errorMessage = e.getMessage();
            logger.error("Error adding feedback item: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    public Retrospective getRetrospectiveByName(String name) {
        for (Retrospective retrospective : retrospectives.values()) {
            if (retrospective.getName().equals(name)) {
                return retrospective;
            }
        }
        return null; // Return null if the retrospective with the given name is not found
    }

    public boolean isParticipantInRetrospective(String retrospectiveName, String participantName) {
        Retrospective retrospective = getRetrospectiveByName(retrospectiveName);
        if (retrospective == null) {
            return false;
        }

        List<String> participants = retrospective.getParticipants();
        return participants != null && participants.contains(participantName);
    }
    public Retrospective updateFeedbackItem(String name, Feedback feedback) {
        logger.info("Updating feedback item for retrospective: {}", name);
        Retrospective retrospective = getRetrospectiveByName(name);
        if (retrospective == null) {
            logger.error("Retrospective with name {} does not exist.", name);
            throw new IllegalArgumentException(STR."Retrospective with name \{name} does not exist.");
        }
        retrospective.updateFeedbackItem(name, feedback);
        logger.info("Feedback item updated for retrospective: {}", name);
        return retrospective;
    }

    public List<Retrospective> getAllRetrospectives(int page, int pageSize) {
        logger.debug("Getting all retrospectives. Page: {}, PageSize: {}", page, pageSize);
        List<Retrospective> allRetrospectives = new ArrayList<>(retrospectives.values());
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allRetrospectives.size());
        List<Retrospective> pagedRetrospectives = allRetrospectives.subList(fromIndex, toIndex);
        logger.debug("Retrieved {} retrospectives for page {} with pageSize {}", pagedRetrospectives.size(), page, pageSize);
        return pagedRetrospectives;
    }

    public List<Retrospective> searchRetrospectivesByDate(String date) {
        logger.info("Searching retrospectives for date: {}", date);
        List<Retrospective> matchingRetrospectives = new ArrayList<>();
        for (Retrospective retrospective : retrospectives.values()) {
            if (retrospective.getDate().equals(date)) {
                matchingRetrospectives.add(retrospective);
            }
        }
        logger.info("Retrospectives found for date {}: {}", date, matchingRetrospectives.size());
        return matchingRetrospectives;
    }
}
