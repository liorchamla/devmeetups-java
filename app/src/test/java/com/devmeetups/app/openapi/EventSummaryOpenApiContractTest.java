package com.devmeetups.app.openapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devmeetups.app.events.repository.EventsRepository;
import com.devmeetups.app.registrations.repository.RegistrationsRepository;

@SpringBootTest(properties = {
    "spring.flyway.enabled=false",
    "spring.autoconfigure.exclude=" +
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
        "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
        "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration," +
        "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
@AutoConfigureMockMvc(addFilters = false)
class EventSummaryOpenApiContractTest {

  @Autowired
  MockMvc mvc;

  // 🔧 Mocke ce que tes controllers injectent (service ou repo)
  @MockBean
  EventsRepository eventsRepository;
  @MockBean
  RegistrationsRepository registrationsRepository;

  @Test
  void eventSummary_schema_has_expected_properties() throws Exception {
    mvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        // Schéma présent
        .andExpect(jsonPath("$.components.schemas.EventSummary").exists())
        .andExpect(jsonPath("$.components.schemas.EventSummary.type").value("object"))
        // slug
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.slug.type").value("string"))
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.slug.description")
            .value("Slug unique de l'événement"))
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.slug.example").value("spring-boot-kickoff"))
        // title
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.title.type").value("string"))
        // liveAt : string date-time
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.liveAt.type").value("string"))
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.liveAt.format").value("date-time"))
        // location
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.location.type").value("string"))
        // price : number (on ne checke pas le format volontairement)
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.price.type").value("number"))
        // featured
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.featured.type").value("boolean"))
        // imageUrl
        .andExpect(jsonPath("$.components.schemas.EventSummary.properties.imageUrl.type").value("string"));
  }

  @Test
  void events_paths_reference_eventSummary_schema() throws Exception {
    mvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        // GET /events -> array d'EventSummary
        .andExpect(
            jsonPath("$.paths['/events'].get.responses['200'].content['application/json'].schema.type").value("array"))
        .andExpect(jsonPath("$.paths['/events'].get.responses['200'].content['application/json'].schema.items['$ref']")
            .value("#/components/schemas/EventSummary"));
  }
}
