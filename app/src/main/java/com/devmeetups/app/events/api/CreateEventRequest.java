package com.devmeetups.app.events.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record CreateEventRequest(
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "slug must be kebab-case") @Size(min = 3, max = 80) String slug,

    @NotBlank @Size(min = 3, max = 120) String title,

    @NotNull Instant liveAt,

    @NotBlank @Size(min = 2, max = 80) String location,

    @NotNull @DecimalMin(value = "0.00") @Digits(integer = 8, fraction = 2) BigDecimal price,

    boolean featured,

    @NotBlank @Size(max = 300) String imageUrl) {
}
