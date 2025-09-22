package com.devmeetups.app.registrations.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devmeetups.app.events.Event;
import com.devmeetups.app.events.repository.EventsRepository;
import com.devmeetups.app.registrations.repository.RegistrationsRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegistrationWebContractTest {
  @Container
  static final MariaDBContainer<?> DB = new MariaDBContainer<>("mariadb:11");

  @DynamicPropertySource
  static void dbProps(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", DB::getJdbcUrl);
    r.add("spring.datasource.username", DB::getUsername);
    r.add("spring.datasource.password", DB::getPassword);
    // Laisse Flyway tourner, Hibernate en validate :
    r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    r.add("spring.jpa.properties.hibernate.jdbc.time_zone", () -> "UTC");
  }

  @Autowired
  MockMvc mvc;

  @Autowired
  RegistrationsRepository registrationsRepository;

  @Autowired
  EventsRepository eventsRepository;

  @BeforeEach
  void seedEvent() {
    eventsRepository.save(new Event(
        "devmeetups-paris", "DevMeetups Paris", null, "Paris",
        new BigDecimal("0.00"), Instant.now().plusSeconds(86_400), true, null));
  }

  @Test
  @Transactional
  void happy_path() throws Exception {
    mvc.perform(post("/events/devmeetups-paris/registrations")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"alice@mail.test\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.eventSlug").value("devmeetups-paris"))
        .andExpect(jsonPath("$.email").value("alice@mail.test"));
  }

  @Test
  @Transactional
  void bad_request_missing_email() throws Exception {
    mvc.perform(post("/events/devmeetups-paris/registrations")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void bad_request_bad_email() throws Exception {
    mvc.perform(post("/events/devmeetups-paris/registrations")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"not-an-email\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Transactional
  void bad_request_conflict() throws Exception {
    mvc.perform(post("/events/devmeetups-paris/registrations")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"charlie@mail.test\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.eventSlug").value("devmeetups-paris"))
        .andExpect(jsonPath("$.email").value("charlie@mail.test"));

    mvc.perform(post("/events/devmeetups-paris/registrations")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"charlie@mail.test\"}"))
        .andExpect(status().isConflict());
  }
}
