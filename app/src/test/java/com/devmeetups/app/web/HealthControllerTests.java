package com.devmeetups.app.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class HealthControllerTest {

  static Stream<String> statuses() {
    return Stream.of("OK", "OK"); // placeholder pour montrer la paramétrisation
  }

  @ParameterizedTest
  @MethodSource("statuses")
  void returnsOkForHealth(String expected) {
    var ctrl = new HealthController();
    var res = ctrl.health();
    assertThat(res.status()).isEqualTo(expected);
  }
}
