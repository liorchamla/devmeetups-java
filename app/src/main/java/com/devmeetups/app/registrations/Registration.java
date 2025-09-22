package com.devmeetups.app.registrations;

import java.time.Instant;

import com.devmeetups.app.events.Event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Registration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  @Column(nullable = false)
  String email;

  @Column(nullable = false)
  Instant registeredAt;

  public Registration(Event event, String email) {
    this.event = event;
    this.email = email;
    this.registeredAt = Instant.now();
  }

  public Registration(Event event, String email, Instant registeredAt) {
    this.event = event;
    this.email = email;
    this.registeredAt = registeredAt;
  }

  public Long getId() {
    return id;
  }

  public Long eventId() {
    return event != null ? event.getId() : null;
  }

  public String email() {
    return email;
  }

  public Instant registeredAt() {
    return registeredAt;
  }
}
