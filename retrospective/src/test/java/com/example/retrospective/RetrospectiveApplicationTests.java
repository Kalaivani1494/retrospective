package com.example.retrospective;

import ch.qos.logback.classic.Logger;
import com.example.retrospective.controller.RetrospectiveController;
import com.example.retrospective.model.Feedback;
import com.example.retrospective.model.Retrospective;
import com.example.retrospective.service.RetrospectiveService;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class RetrospectiveApplicationTests {

	@Mock
	private RetrospectiveService retrospectiveService;

	@InjectMocks
	private RetrospectiveController retrospectiveController;

	public void RetrospectiveControllerTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAddFeedbackItem_Success() throws CustomExceptionHandler.ParticipantNotAssociatedException {
		// Mock the retrospectiveService to return an updated retrospective

// Arrange
		String retrospectiveName = "retroName";
		Feedback feedback = new Feedback();
		Retrospective retrospective = new Retrospective();
		when(retrospectiveService.getRetrospectiveByName(retrospectiveName)).thenReturn(retrospective);
		when(retrospectiveService.isParticipantInRetrospective(retrospectiveName, feedback.getName())).thenReturn(true);

		// Act
		ResponseEntity<?> response = retrospectiveController.addFeedbackItem(retrospectiveName, feedback);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNull(response.getBody());
	}
//	@Test
//	void testAddFeedbackItem_ParticipantNotAssociated() throws CustomExceptionHandler.ParticipantNotAssociatedException {
//		// Arrange
//		String retrospectiveName = "retroName";
//		String participantName = "participant1";
//		when(retrospectiveService.isParticipantInRetrospective(eq(retrospectiveName), eq(participantName)))
//				.thenThrow(new CustomExceptionHandler.ParticipantNotAssociatedException("Participant not associated"));
//
//		// Act and Assert
//		assertThrows(CustomExceptionHandler.ParticipantNotAssociatedException.class, () -> {
//			retrospectiveController.addFeedbackItem(retrospectiveName, new Feedback());
//		});
//	}

}