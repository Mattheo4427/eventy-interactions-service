CREATE TABLE conversations (
    id VARCHAR(255) PRIMARY KEY,
    participant1_id VARCHAR(255) NOT NULL,
    participant2_id VARCHAR(255) NOT NULL,
    related_ticket_id VARCHAR(255),
    related_event_id VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_conversations_participants ON conversations(participant1_id, participant2_id);
