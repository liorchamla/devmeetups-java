ALTER TABLE registrations RENAME TO registration;
ALTER TABLE events RENAME TO event;
CREATE INDEX idx_registration_event_id ON registration(event_id);
CREATE INDEX idx_registration_email ON registration(email);

ALTER TABLE registration
DROP CONSTRAINT fk_event_id;
ALTER TABLE registration
ADD CONSTRAINT fk_event_id
FOREIGN KEY (event_id)
REFERENCES event(id)
ON DELETE CASCADE;
