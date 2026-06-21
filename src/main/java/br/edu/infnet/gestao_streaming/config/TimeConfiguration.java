package br.edu.infnet.gestao_streaming.config;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TimeConfiguration {

  @Bean
  Clock clock(@Value("${app.clock.fixed-date:}") String fixedDate) {
    if (fixedDate != null && !fixedDate.isBlank()) {
      ZoneId zone = ZoneId.systemDefault();
      return Clock.fixed(LocalDate.parse(fixedDate).atStartOfDay(zone).toInstant(), zone);
    }

    return Clock.systemDefaultZone();
  }
}
