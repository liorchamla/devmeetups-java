package com.devmeetups.app.registrations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devmeetups.app.registrations.Registration;

public interface RegistrationsRepository extends JpaRepository<Registration, Long> {
  boolean existsByEventIdAndEmail(Long eventId, String email);

  Optional<Registration> findByEventSlugAndEmail(String slug, String email);
}
