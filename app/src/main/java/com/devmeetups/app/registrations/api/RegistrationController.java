package com.devmeetups.app.registrations.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devmeetups.app.events.Event;
import com.devmeetups.app.events.repository.EventsRepository;
import com.devmeetups.app.registrations.Registration;
import com.devmeetups.app.registrations.repository.RegistrationsRepository;

import jakarta.validation.Valid;

@RestController
public class RegistrationController {

  List<String> registeredEmails = new ArrayList<>();

  final RegistrationsRepository registrationsRepository;
  final EventsRepository eventsRepository;

  RegistrationController(RegistrationsRepository registrationsRepository, EventsRepository eventsRepository) {
    this.registrationsRepository = registrationsRepository;
    this.eventsRepository = eventsRepository;
  }

  @PostMapping("/events/{eventSlug}/registrations")
  @ResponseStatus(HttpStatus.CREATED)
  public CreateRegistrationResponse register(@PathVariable String eventSlug,
      @Valid @RequestBody CreateRegistrationRequest body) {

    Optional<Event> event = eventsRepository.findBySlug(eventSlug);

    if (!event.isPresent()) {
      throw new NoSuchElementException("Event with slug " + eventSlug + " not found");
    }

    if (registrationsRepository.existsByEventIdAndEmail(event.get().getId(), body.email())) {
      throw new IllegalStateException("Email " + body.email() + " already registered for event " + eventSlug);
    }

    registrationsRepository.save(new Registration(event.get(), body.email(), Instant.now()));
    return new CreateRegistrationResponse(eventSlug, body.email());
  }

}
