package br.edu.infnet.gestao_streaming.external.tmdb;

import br.edu.infnet.gestao_streaming.external.CatalogProviderGateway;
import br.edu.infnet.gestao_streaming.external.model.ExternalMovie;
import br.edu.infnet.gestao_streaming.external.model.ExternalMovieCatalog;
import br.edu.infnet.gestao_streaming.external.model.ExternalStreamingProvider;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbMovieListResponse.TmdbMovieResponse;
import br.edu.infnet.gestao_streaming.external.tmdb.TmdbProviderListResponse.TmdbProviderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TmdbCatalogAdapter implements CatalogProviderGateway {

  private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

  private final TmdbApiClient client;

  @Override
  public List<ExternalStreamingProvider> listMovieProviders(String region) {
    TmdbProviderListResponse response = client.listMovieProviders(region);
    if (response.results() == null) {
      return List.of();
    }
    return response.results().stream().map(this::adaptProvider).toList();
  }

  @Override
  public ExternalMovieCatalog discoverMoviesByProvider(Integer providerId, String region) {
    TmdbMovieListResponse response = client.discoverMoviesByProvider(providerId, region);
    List<ExternalMovie> movies =
        response.results() == null
            ? List.of()
            : response.results().stream().map(this::adaptMovie).toList();

    return new ExternalMovieCatalog(
        response.page(), response.totalPages(), response.totalResults(), movies);
  }

  private ExternalStreamingProvider adaptProvider(TmdbProviderResponse provider) {
    return new ExternalStreamingProvider(
        provider.providerId(),
        provider.providerName(),
        buildImageUrl(provider.logoPath()),
        provider.displayPriority());
  }

  private ExternalMovie adaptMovie(TmdbMovieResponse movie) {
    return new ExternalMovie(
        movie.id(),
        movie.title(),
        movie.overview(),
        buildImageUrl(movie.posterPath()),
        movie.releaseDate(),
        movie.voteAverage(),
        movie.popularity());
  }

  private String buildImageUrl(String imagePath) {
    if (imagePath == null || imagePath.isBlank()) {
      return null;
    }
    return IMAGE_BASE_URL + imagePath;
  }
}
