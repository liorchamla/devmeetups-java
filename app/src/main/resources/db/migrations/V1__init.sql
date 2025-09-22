
CREATE TABLE events (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slug VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(300) NOT NULL,
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    live_at TIMESTAMP NOT NULL,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    image_url VARCHAR(512)
);
CREATE INDEX idx_events_live_at ON events(live_at);
CREATE INDEX idx_events_location ON events(location);


CREATE TABLE registrations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    email VARCHAR(255) NOT NULL,
    registered_at TIMESTAMP NOT NULL,
    UNIQUE (event_id, email)
);

ALTER TABLE registrations
ADD CONSTRAINT fk_event_id
FOREIGN KEY (event_id)
REFERENCES events(id)
ON DELETE CASCADE;
