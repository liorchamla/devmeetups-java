package com.devmeetups.app.events.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devmeetups.app.events.Event;

@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
  Optional<Event> findBySlug(String slug);

  Page<Event> findByLiveAtAfter(Instant now, PageRequest pageRequest);
}
