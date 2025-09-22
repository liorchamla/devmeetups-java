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
class OpenApiContractTests {

  @Autowired
  MockMvc mvc;

  // 🔧 Mocke ce que tes controllers injectent (service ou repo)
  @MockBean
  EventsRepository eventsRepository;
  @MockBean
  RegistrationsRepository registrationsRepository;

  @Test
  void api_docs_exposes_events_paths() throws Exception {
    mvc.perform(get("/v3/api-docs"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.paths['/events']").exists())
        .andExpect(jsonPath("$.info.title").value("DevMeetups API"));
  }
}
