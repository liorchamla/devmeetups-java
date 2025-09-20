package com.devmeetups.app.events.api;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CreateEventRequestValidationTest {

  private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  static Stream<CreateEventRequest> invalidRequests() {
    return Stream.of(
        new CreateEventRequest("Invalid SLUG", "Title", Instant.now(), "Paris", new BigDecimal("10.00"), false, "img"),
        new CreateEventRequest("ok", "", Instant.now(), "Paris", new BigDecimal("10.00"), false, "img"),
        new CreateEventRequest("ok", "Ok title", null, "Paris", new BigDecimal("10.00"), false, "img"),
        new CreateEventRequest("ok", "Ok title", Instant.now(), "", new BigDecimal("10.00"), false, "img"),
        new CreateEventRequest("ok", "Ok title", Instant.now(), "Paris", new BigDecimal("-1.00"), false, "img"));
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void should_fail_validation(CreateEventRequest req) {
    Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(req);
    assertThat(violations).isNotEmpty();
  }

  static Stream<CreateEventRequest> validRequests() {
    return Stream.of(
        new CreateEventRequest("devmeetups-paris", "DevMeetups Paris",
            Instant.now().plusSeconds(3600), "Paris", new BigDecimal("12.50"), true, "https://img.example/1.png"));
  }

  @ParameterizedTest
  @MethodSource("validRequests")
  void should_pass_validation(CreateEventRequest req) {
    var violations = validator.validate(req);
    assertThat(violations).isEmpty();
  }
}
