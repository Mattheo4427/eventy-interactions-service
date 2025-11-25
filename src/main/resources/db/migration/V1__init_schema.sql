-- 1. Table des Messages
CREATE TABLE messages (
                          message_id BIGSERIAL PRIMARY KEY,
                          sender_id VARCHAR(255) NOT NULL,
                          receiver_id VARCHAR(255) NOT NULL,
                          transaction_id UUID,

                          subject VARCHAR(255) NOT NULL,
                          content TEXT NOT NULL,
                          date_sent TIMESTAMP,

                          is_read BOOLEAN NOT NULL DEFAULT FALSE,
                          read_date TIMESTAMP,

                          message_type VARCHAR(50),
                          conversation_id VARCHAR(255),
                          related_ticket_id VARCHAR(255),
                          related_event_id VARCHAR(255)
);

-- 2. Table des Signalements (Reports)
CREATE TABLE reports (
                         report_id BIGSERIAL PRIMARY KEY,
                         reporter_id VARCHAR(255) NOT NULL,
                         reported_user_id VARCHAR(255),

                         reported_ticket_id UUID,      -- Attention: Ticket Service utilise UUID
                         reported_transaction_id UUID, -- Attention: Transaction Service utilise UUID

                         report_type VARCHAR(50) NOT NULL,
                         reason TEXT NOT NULL,
                         evidence TEXT,
                         report_date TIMESTAMP,
                         admin_action TEXT,
                         status VARCHAR(50) NOT NULL, -- PENDING, RESOLVED...
                         priority VARCHAR(50),

                         admin_notes TEXT,
                         admin_id VARCHAR(255)

);

-- 3. Table des Avis (Reviews)
CREATE TABLE reviews (
                         review_id BIGSERIAL PRIMARY KEY,
                         author_id VARCHAR(255) NOT NULL,
                         receiver_id VARCHAR(255) NOT NULL,
                         transaction_id UUID NOT NULL, -- Attention: Transaction Service utilise UUID

                         rating INTEGER NOT NULL,
                         comment TEXT,
                         creation_date TIMESTAMP,

                         status VARCHAR(50) NOT NULL,
                         review_type VARCHAR(50) NOT NULL
);

-- Index
CREATE INDEX idx_messages_conversation ON messages(conversation_id);
CREATE INDEX idx_messages_users ON messages(sender_id, receiver_id);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reviews_receiver ON reviews(receiver_id);