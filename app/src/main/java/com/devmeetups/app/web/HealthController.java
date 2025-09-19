package com.devmeetups.app.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

record HealthResponse(String status) {
}

@RestController
public class HealthController {
  @GetMapping("/health")
  public HealthResponse health() {
    return new HealthResponse("OK");
  }
}
