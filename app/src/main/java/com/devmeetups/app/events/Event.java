package com.devmeetups.app.events;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(unique = true, nullable = false)
  String slug;

  @Column(nullable = false)
  String title;

  @Column(columnDefinition = "TEXT")
  String description;

  @Column(nullable = false)
  String location;

  @Column(nullable = false)
  BigDecimal price;

  @Column(nullable = false)
  Instant liveAt;

  @Column(nullable = false)
  boolean featured;

  @Column(nullable = false)
  String imageUrl;

  // --- JPA a besoin d’un no-args ctor (au moins protected) ---
  protected Event() {
  }

  // constructeur “complet” utile pour créer l’entity côté service
  public Event(String slug, String title, String description, String location,
      BigDecimal price, Instant liveAt, boolean featured, String imageUrl) {
    this.slug = slug;
    this.title = title;
    this.description = description;
    this.location = location;
    this.price = price;
    this.liveAt = liveAt;
    this.featured = featured;
    this.imageUrl = imageUrl;
  }

  // Getters (field access fonctionne aussi, mais c’est sain d’exposer des
  // accesseurs)
  public Long getId() {
    return id;
  }

  public String getSlug() {
    return slug;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public Instant getLiveAt() {
    return liveAt;
  }

  public boolean isFeatured() {
    return featured;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  // Setters optionnels si tu as des cas d’update ; sinon garde l’entité plutôt
  // “immutablesque”.
  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public void setLiveAt(Instant liveAt) {
    this.liveAt = liveAt;
  }

  public void setFeatured(boolean featured) {
    this.featured = featured;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
