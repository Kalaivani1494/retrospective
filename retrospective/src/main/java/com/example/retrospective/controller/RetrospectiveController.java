package com.example.retrospective.controller;

import com.example.retrospective.CustomExceptionHandler;
import com.example.retrospective.model.Retrospective;
import com.example.retrospective.model.Feedback;
import com.example.retrospective.service.RetrospectiveServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@Validated
@RequestMapping("/api/retrospectives")
public class RetrospectiveController {
    private final Logger logger = LoggerFactory.getLogger(RetrospectiveController.class);

    @Autowired
    private RetrospectiveServiceInterface retrospectiveService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createRetrospective(@RequestBody Retrospective retrospective) {
        logger.info("Creating a new retrospective: {}", retrospective);
        ResponseEntity<?> createdRetrospective = retrospectiveService.createRetrospective(retrospective);
        logger.info("Retrospective created with Name: {}", retrospective.getName());
        return new ResponseEntity<>(createdRetrospective, HttpStatus.CREATED);
    }

    @PostMapping("/{name}/feedback")
    public ResponseEntity<?>  addFeedbackItem(@PathVariable String name, @RequestBody Feedback feedback) {
        logger.info("Adding feedback item for retrospective: {}", name);
        try {
        ResponseEntity<?> updatedRetrospective = retrospectiveService.addFeedbackItem(name, feedback);
        logger.debug("Feedback item added successfully");
        return ResponseEntity.ok(updatedRetrospective);
    } catch (CustomExceptionHandler.ParticipantNotAssociatedException e) {
            String errorMessage = e.getMessage();
            logger.error("Failed to add feedback item: {}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/{name}/feedback/update")
    public ResponseEntity<Retrospective> updateFeedbackItem(@PathVariable String name, @RequestBody Feedback feedback) {
        Retrospective updatedRetrospective = retrospectiveService.updateFeedbackItem(name, feedback);
        return new ResponseEntity<>(updatedRetrospective, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Retrospective>> getAllRetrospectives(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        logger.info("Retrieving all retrospectives");
        List<Retrospective> retrospectives = retrospectiveService.getAllRetrospectives(page, pageSize);
        logger.debug("Retrieved {} retrospectives", retrospectives.size());
        return new ResponseEntity<>(retrospectives, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Retrospective>> searchRetrospectivesByDate(@RequestParam(value = "date") String date,
                                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        List<Retrospective> retrospectives = retrospectiveService.searchRetrospectivesByDate(date, page, pageSize);
        return new ResponseEntity<>(retrospectives, HttpStatus.OK);
    }
}
