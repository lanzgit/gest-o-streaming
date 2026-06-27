package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.config.TmdbProperties;
import br.edu.infnet.gestao_streaming.dto.ExternalMovieSearchResponse;
import br.edu.infnet.gestao_streaming.dto.ExternalStreamingProviderResponse;
import br.edu.infnet.gestao_streaming.external.CatalogProviderGateway;
import br.edu.infnet.gestao_streaming.external.model.ExternalMovie;
import br.edu.infnet.gestao_streaming.external.model.ExternalMovieCatalog;
import br.edu.infnet.gestao_streaming.external.model.ExternalStreamingProvider;
import br.edu.infnet.gestao_streaming.service.ExternalCatalogService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExternalCatalogServiceTests {

  private final FakeCatalogProviderGateway catalogProvider = new FakeCatalogProviderGateway();
  private final TmdbProperties properties = tmdbProperties();
  private final ExternalCatalogService service =
      new ExternalCatalogService(catalogProvider, properties);

  @Test
  void listsMovieProvidersUsingDefaultRegion() {
    catalogProvider.providers =
        List.of(
            new ExternalStreamingProvider(
                8, "Netflix", "https://image.tmdb.org/t/p/w500/netflix.png", 1));

    List<ExternalStreamingProviderResponse> providers = service.listMovieProviders(null);

    assertThat(providers).hasSize(1);
    assertThat(providers.getFirst().providerId()).isEqualTo(8);
    assertThat(providers.getFirst().providerName()).isEqualTo("Netflix");
    assertThat(providers.getFirst().logoUrl())
        .isEqualTo("https://image.tmdb.org/t/p/w500/netflix.png");
    assertThat(catalogProvider.lastProviderRegion).isEqualTo("BR");
  }

  @Test
  void discoversMoviesByProviderUsingRequestedRegion() {
    catalogProvider.movieCatalog =
        new ExternalMovieCatalog(
            1,
            10,
            200,
            List.of(
                new ExternalMovie(
                    550,
                    "Fight Club",
                    "Overview",
                    "https://image.tmdb.org/t/p/w500/poster.jpg",
                    LocalDate.of(1999, 10, 15),
                    8.4,
                    90.0)));

    ExternalMovieSearchResponse response = service.discoverMoviesByProvider(337, "us");

    assertThat(response.page()).isEqualTo(1);
    assertThat(response.totalPages()).isEqualTo(10);
    assertThat(response.totalResults()).isEqualTo(200);
    assertThat(response.movies()).hasSize(1);
    assertThat(response.movies().getFirst().posterUrl())
        .isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg");
    assertThat(catalogProvider.lastMovieProviderId).isEqualTo(337);
    assertThat(catalogProvider.lastMovieRegion).isEqualTo("US");
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

  private static class FakeCatalogProviderGateway implements CatalogProviderGateway {

    private List<ExternalStreamingProvider> providers = List.of();
    private ExternalMovieCatalog movieCatalog;
    private String lastProviderRegion;
    private Integer lastMovieProviderId;
    private String lastMovieRegion;

    @Override
    public List<ExternalStreamingProvider> listMovieProviders(String region) {
      lastProviderRegion = region;
      return providers;
    }

    @Override
    public ExternalMovieCatalog discoverMoviesByProvider(Integer providerId, String region) {
      lastMovieProviderId = providerId;
      lastMovieRegion = region;
      return movieCatalog;
    }
  }
}
