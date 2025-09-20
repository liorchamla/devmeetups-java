package com.devmeetups.app.events;

import java.math.BigDecimal;
import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Résumé d’un événement")
public record EventSummary(
    @Schema(description = "Slug unique de l'événement", example = "spring-boot-kickoff") String slug,
    @Schema(description = "Titre de l'événement", example = "Spring Boot Kickoff") String title,
    @Schema(description = "Date et heure de l'événement", example = "2023-03-15T10:00:00Z") Instant liveAt,
    @Schema(description = "Lieu de l'événement", example = "En ligne") String location,
    @Schema(description = "Prix de l'événement", example = "220.99", type = "number", format = "float") BigDecimal price,
    @Schema(description = "Indique si l'événement est en vedette") boolean featured,
    @Schema(description = "URL de l'image de l'événement", example = "https://pics.example/spring.png") String imageUrl) {
}
