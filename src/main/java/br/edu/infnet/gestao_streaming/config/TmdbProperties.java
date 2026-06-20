package br.edu.infnet.gestao_streaming.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tmdb")
@Getter
@Setter
public class TmdbProperties {

  private String baseUrl;
  private String apiToken;
  private String defaultRegion;
  private String defaultLanguage;
}
