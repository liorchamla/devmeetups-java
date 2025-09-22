package com.devmeetups.app.error;

import com.devmeetups.app.events.api.CreateEventRequest;
import com.devmeetups.app.events.api.EventsController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventsController.class)
@Import(GlobalExceptionHandler.class)
class ErrorContractWebTest {

  @Autowired
  MockMvc mvc;
  @Autowired
  ObjectMapper om;

  @Test
  void validation_error_returns_apierror_with_violations() throws Exception {
    var bad = new CreateEventRequest(
        "INVALID SLUG", "Title", Instant.now(), "Paris", new BigDecimal("10.00"), false, "img");

    mvc.perform(post("/events")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(bad)))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.type").value("validation"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.instance").value("/events"))
        .andExpect(jsonPath("$.extras.violations").isArray());
  }
}
