package com.polytech.interactions_service.service;

import com.polytech.interactions_service.dto.MessageRequest;
import com.polytech.interactions_service.model.Message;
import com.polytech.interactions_service.model.enums.MessageType;
import com.polytech.interactions_service.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMessage_buildsAndSavesMessage() {
        MessageRequest request = new MessageRequest();
        UUID txId = UUID.randomUUID();
        request.setReceiverId("receiver-1");
        request.setTransactionId(txId);
        request.setSubject("Hello");
        request.setContent("Hi there");

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        when(messageRepository.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        messageService.sendMessage("sender-123", request);

        Message saved = captor.getValue();
        assertThat(saved.getSenderId()).isEqualTo("sender-123");
        assertThat(saved.getReceiverId()).isEqualTo("receiver-1");
        assertThat(saved.getTransactionId()).isEqualTo(txId);
        assertThat(saved.getSubject()).isEqualTo("Hello");
        assertThat(saved.getContent()).isEqualTo("Hi there");
        assertThat(saved.getMessageType()).isEqualTo(MessageType.GENERAL);
    }

    @Test
    void getMyInbox_delegatesToRepository() {
        Message msg = Message.builder().build();
        when(messageRepository.findByReceiverIdOrderByDateSentDesc("user-1"))
                .thenReturn(List.of(msg));

        List<Message> result = messageService.getMyInbox("user-1");

        assertThat(result).hasSize(1).containsExactly(msg);
        verify(messageRepository).findByReceiverIdOrderByDateSentDesc("user-1");
    }

    @Test
    void markAsRead_whenMessageExists_marksAsReadAndSaves() {
        Message msg = Message.builder().build();
        when(messageRepository.findById(5L)).thenReturn(Optional.of(msg));

        messageService.markAsRead(5L);

        verify(messageRepository).save(msg);
    }

    @Test
    void markAsRead_whenMessageMissing_doesNothing() {
        when(messageRepository.findById(42L)).thenReturn(Optional.empty());

        messageService.markAsRead(42L);

        verify(messageRepository, never()).save(any());
    }
}
