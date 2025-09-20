package com.devmeetups.app.events.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EventsController.class)
class EventsControllerWebTest {

  @Autowired
  MockMvc mvc;
  @Autowired
  ObjectMapper om;

  @Test
  void get_events_ok() throws Exception {
    mvc.perform(get("/events"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].slug").value("spring-boot-kickoff"));
  }

  @Test
  void post_event_validation_error() throws Exception {
    var bad = new CreateEventRequest(
        "INVALID SLUG", "Title", Instant.now(), "Paris", new BigDecimal("10.00"), false, "img");
    mvc.perform(post("/events")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(bad)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void post_event_created() throws Exception {
    var ok = new CreateEventRequest(
        "devmeetups-lyon", "DevMeetups Lyon",
        Instant.now().plusSeconds(7200), "Lyon",
        new BigDecimal("0.00"), true, "https://img.example/2.png");

    mvc.perform(post("/events")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(ok)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.slug").value("devmeetups-lyon"));
  }
}
