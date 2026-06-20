package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.config.TmdbProperties;
import br.edu.infnet.gestao_streaming.dto.ExternalMovieSearchResponse;
import br.edu.infnet.gestao_streaming.dto.ExternalStreamingProviderResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbCatalogGateway;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse.TmdbMovieResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbProviderListResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbProviderListResponse.TmdbProviderResponse;
import br.edu.infnet.gestao_streaming.service.ExternalCatalogService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExternalCatalogServiceTests {

  private final FakeTmdbCatalogGateway tmdbClient = new FakeTmdbCatalogGateway();
  private final TmdbProperties properties = tmdbProperties();
  private final ExternalCatalogService service = new ExternalCatalogService(tmdbClient, properties);

  @Test
  void listsMovieProvidersUsingDefaultRegion() {
    tmdbClient.providerListResponse =
        new TmdbProviderListResponse(
            List.of(new TmdbProviderResponse(8, "Netflix", "/netflix.png", 1)));

    List<ExternalStreamingProviderResponse> providers = service.listMovieProviders(null);

    assertThat(providers).hasSize(1);
    assertThat(providers.getFirst().providerId()).isEqualTo(8);
    assertThat(providers.getFirst().providerName()).isEqualTo("Netflix");
    assertThat(providers.getFirst().logoUrl())
        .isEqualTo("https://image.tmdb.org/t/p/w500/netflix.png");
    assertThat(tmdbClient.lastProviderRegion).isEqualTo("BR");
  }

  @Test
  void discoversMoviesByProviderUsingRequestedRegion() {
    tmdbClient.movieListResponse =
        new TmdbMovieListResponse(
            1,
            List.of(
                new TmdbMovieResponse(
                    550,
                    "Fight Club",
                    "Overview",
                    "/poster.jpg",
                    LocalDate.of(1999, 10, 15),
                    8.4,
                    90.0)),
            10,
            200);

    ExternalMovieSearchResponse response = service.discoverMoviesByProvider(337, "us");

    assertThat(response.page()).isEqualTo(1);
    assertThat(response.totalPages()).isEqualTo(10);
    assertThat(response.totalResults()).isEqualTo(200);
    assertThat(response.movies()).hasSize(1);
    assertThat(response.movies().getFirst().posterUrl())
        .isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg");
    assertThat(tmdbClient.lastMovieProviderId).isEqualTo(337);
    assertThat(tmdbClient.lastMovieRegion).isEqualTo("US");
  }

  @Test
  void providerIdMustBePositive() {
    assertThatThrownBy(() -> service.discoverMoviesByProvider(0, "BR"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("providerId deve ser positivo.");
  }

  private TmdbProperties tmdbProperties() {
    TmdbProperties properties = new TmdbProperties();
    properties.setBaseUrl("https://api.themoviedb.org/3");
    properties.setApiToken("test-token");
    properties.setDefaultRegion("BR");
    properties.setDefaultLanguage("pt-BR");
    return properties;
  }

  private static class FakeTmdbCatalogGateway implements TmdbCatalogGateway {

    private TmdbProviderListResponse providerListResponse;
    private TmdbMovieListResponse movieListResponse;
    private String lastProviderRegion;
    private Integer lastMovieProviderId;
    private String lastMovieRegion;

    @Override
    public TmdbProviderListResponse listMovieProviders(String region) {
      lastProviderRegion = region;
      return providerListResponse;
    }

    @Override
    public TmdbMovieListResponse discoverMoviesByProvider(Integer providerId, String region) {
      lastMovieProviderId = providerId;
      lastMovieRegion = region;
      return movieListResponse;
    }
  }
}
