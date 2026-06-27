package br.edu.infnet.gestao_streaming.external;

import br.edu.infnet.gestao_streaming.external.model.ExternalMovieCatalog;
import br.edu.infnet.gestao_streaming.external.model.ExternalStreamingProvider;
import java.util.List;

public interface CatalogProviderGateway {

  List<ExternalStreamingProvider> listMovieProviders(String region);

  ExternalMovieCatalog discoverMoviesByProvider(Integer providerId, String region);
}
