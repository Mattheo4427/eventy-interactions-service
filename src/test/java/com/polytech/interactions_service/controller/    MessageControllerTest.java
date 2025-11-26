package com.polytech.interactions_service.controller;

import com.polytech.interactions_service.dto.MessageRequest;
import com.polytech.interactions_service.model.Message;
import com.polytech.interactions_service.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MessageControllerTest {

    @Mock
    private MessageService messageService;

    private MessageController messageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageController = new MessageController(messageService);
    }

    private Jwt jwtWithSubject(String subject) {
        return Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject(subject)
                .build();
    }

    @Test
    void sendMessage_delegatesToService_andReturnsCreated() {
        MessageRequest request = new MessageRequest();
        request.setReceiverId("receiver-1");
        request.setSubject("Hello");
        request.setContent("Hi!");

        Jwt jwt = jwtWithSubject("sender-123");

        ResponseEntity<Void> response = messageController.sendMessage(request, jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(messageService).sendMessage("sender-123", request);
    }

    @Test
    void getMyMessages_returnsMessagesFromService() {
        Jwt jwt = jwtWithSubject("user-42");
        List<Message> messages = List.of(Message.builder().build());
        when(messageService.getMyInbox("user-42")).thenReturn(messages);

        ResponseEntity<List<Message>> response = messageController.getMyMessages(jwt);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(messages);
        verify(messageService).getMyInbox("user-42");
    }

    @Test
    void markAsRead_delegatesToService_andReturnsOk() {
        ResponseEntity<Void> response = messageController.markAsRead(10L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(messageService).markAsRead(10L);
    }
}
