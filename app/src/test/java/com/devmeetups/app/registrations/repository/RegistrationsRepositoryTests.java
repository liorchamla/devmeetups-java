package com.devmeetups.app.registrations.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devmeetups.app.events.Event;
import com.devmeetups.app.events.repository.EventsRepository;
import com.devmeetups.app.registrations.Registration;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegistrationsRepositoryTests {

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
  RegistrationsRepository repo;
  @Autowired
  EventsRepository eventsRepo;

  @BeforeEach
  void seed() {
    repo.deleteAll();
    eventsRepo.deleteAll();

    // On doit avoir des events pour les FK :
    Event event1 = eventsRepo.save(new Event(
        "spring-boot-kickoff", "Spring Boot Kickoff", null, "Paris",
        new BigDecimal("0.00"), Instant.now().plusSeconds(86_400), true, null));

    Event event2 = eventsRepo.save(new Event(
        "kafka-intro", "Kafka Intro", null, "Lyon",
        new BigDecimal("25.00"), Instant.now().plusSeconds(172_800), false, null));

    Event event3 = eventsRepo.save(new Event(
        "hexagonal-arch", "Hexagonal Architecture", null, "Paris",
        new BigDecimal("10.00"), Instant.now().plusSeconds(259_200), true, null));

    repo.save(new Registration(
        event1, "alice@mail.test", Instant.now()));

    repo.save(new Registration(
        event2, "bob@mail.test", Instant.now()));

    repo.save(new Registration(
        event3, "charlie@mail.test", Instant.now()));
  }

  @Test
  void existsByEventIdAndEmail() {
    var exists = repo.existsByEventIdAndEmail(1L, "alice@mail.test");
    assertThat(exists).isTrue();
  }
}
