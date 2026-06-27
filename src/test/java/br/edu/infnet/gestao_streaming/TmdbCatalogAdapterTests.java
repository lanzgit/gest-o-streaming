package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.infnet.gestao_streaming.external.model.ExternalMovieCatalog;
import br.edu.infnet.gestao_streaming.external.model.ExternalStreamingProvider;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbApiClient;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbCatalogAdapter;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse.TmdbMovieResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbProviderListResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbProviderListResponse.TmdbProviderResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TmdbCatalogAdapterTests {

  private final FakeTmdbClient client = new FakeTmdbClient();
  private final TmdbCatalogAdapter adapter = new TmdbCatalogAdapter(client);

  @Test
  void adaptsTmdbProvidersToInternalProviderModel() {
    client.providerListResponse =
        new TmdbProviderListResponse(
            List.of(new TmdbProviderResponse(8, "Netflix", "/netflix.png", 1)));

    List<ExternalStreamingProvider> providers = adapter.listMovieProviders("BR");

    assertThat(providers).hasSize(1);
    assertThat(providers.getFirst().providerId()).isEqualTo(8);
    assertThat(providers.getFirst().providerName()).isEqualTo("Netflix");
    assertThat(providers.getFirst().logoUrl())
        .isEqualTo("https://image.tmdb.org/t/p/w500/netflix.png");
    assertThat(client.lastProviderRegion).isEqualTo("BR");
  }

  @Test
  void adaptsTmdbMoviesToInternalCatalogModel() {
    client.movieListResponse =
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

    ExternalMovieCatalog catalog = adapter.discoverMoviesByProvider(337, "US");

    assertThat(catalog.page()).isEqualTo(1);
    assertThat(catalog.totalPages()).isEqualTo(10);
    assertThat(catalog.totalResults()).isEqualTo(200);
    assertThat(catalog.movies()).hasSize(1);
    assertThat(catalog.movies().getFirst().posterUrl())
        .isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg");
    assertThat(client.lastMovieProviderId).isEqualTo(337);
    assertThat(client.lastMovieRegion).isEqualTo("US");
  }

  @Test
  void adaptsNullTmdbListsToEmptyLists() {
    client.providerListResponse = new TmdbProviderListResponse(null);
    client.movieListResponse = new TmdbMovieListResponse(1, null, 0, 0);

    assertThat(adapter.listMovieProviders("BR")).isEmpty();
    assertThat(adapter.discoverMoviesByProvider(8, "BR").movies()).isEmpty();
  }

  private static class FakeTmdbClient implements TmdbApiClient {

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
