package com.devmeetups.app.events.api;

import com.devmeetups.app.events.EventSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

  private final List<EventSummary> inMemory = new ArrayList<>(
      List.of(new EventSummary(
          "spring-boot-kickoff",
          "Spring Boot Kickoff",
          Instant.now().plusSeconds(86400),
          "Online",
          new BigDecimal("0.00"),
          true,
          "https://pics.example/spring.png")));

  @GetMapping
  @Operation(summary = "Lister les événements", description = "Retourne une liste résumée des événements", tags = {
      "events" }, responses = {
          @ApiResponse(responseCode = "200", description = "Liste des événements récupérée avec succès", content = @Content(mediaType = "application/json", array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = EventSummary.class))))
      })

  List<EventSummary> list() {
    return inMemory;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Créer un événement", description = "Crée un événement après validation", tags = { "events" })
  @ApiResponse(responseCode = "400", description = "Erreur de validation", content = @Content(schema = @Schema(implementation = com.devmeetups.app.error.ErrorDoc.class)))
  EventSummary create(
      @Valid @RequestBody @Parameter(content = @Content(mediaType = "application/json")) CreateEventRequest req) {
    // mapping trivial pour l’instant
    var created = new EventSummary(
        req.slug(), req.title(),
        req.liveAt(), req.location(), req.price(), req.featured(), req.imageUrl());
    inMemory.add(created);
    return created;
  }
}
