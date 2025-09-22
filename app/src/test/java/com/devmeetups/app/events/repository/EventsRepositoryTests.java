package com.devmeetups.app.events.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devmeetups.app.events.Event;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventsRepositoryTests {

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
  EventsRepository repo;

  @BeforeEach
  void seed() {
    repo.deleteAll();

    repo.save(new Event(
        "spring-boot-kickoff", "Spring Boot Kickoff", null, "Paris",
        new BigDecimal("0.00"), Instant.now().plusSeconds(86_400), true, null));

    repo.save(new Event(
        "kafka-intro", "Kafka Intro", null, "Lyon",
        new BigDecimal("25.00"), Instant.now().plusSeconds(172_800), false, null));

    repo.save(new Event(
        "hexagonal-arch", "Hexagonal Architecture", null, "Paris",
        new BigDecimal("10.00"), Instant.now().plusSeconds(259_200), true, null));
  }

  @Test
  void save_and_findBySlug() {
    var found = repo.findBySlug("spring-boot-kickoff");
    assertThat(found).isPresent();
    assertThat(found.get().getLocation()).isEqualTo("Paris");
    assertThat(found.get().getId()).isNotNull();
  }

  @Test
  void pagination_and_sort_by_liveAt_desc() {
    var page = repo.findAll(PageRequest.of(0, 2, Sort.by("liveAt").descending()));
    assertThat(page.getContent()).hasSize(2);
    assertThat(page.getTotalElements()).isEqualTo(3);
    assertThat(page.getContent().get(0).getSlug()).isEqualTo("hexagonal-arch"); // liveAt le + loin
  }

  @Test
  void filter_liveAt_after_now() {
    var page = repo.findByLiveAtAfter(Instant.now(), PageRequest.of(0, 10));
    assertThat(page.getTotalElements()).isEqualTo(3);
  }
}
