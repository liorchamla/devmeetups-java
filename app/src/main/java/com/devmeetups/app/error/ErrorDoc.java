package com.devmeetups.app.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;

@Schema(name = "ApiError", description = "Erreur normalisée")
public record ErrorDoc(
    @Schema(example = "validation") String type,
    @Schema(example = "Validation failed") String title,
    @Schema(example = "400") int status,
    String detail,
    @Schema(example = "/events") String instance,
    Instant timestamp,
    Map<String, Object> extras) {
}
