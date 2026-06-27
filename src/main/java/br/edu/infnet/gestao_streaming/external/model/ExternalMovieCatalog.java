package br.edu.infnet.gestao_streaming.external.model;

import java.util.List;

public record ExternalMovieCatalog(
    Integer page, Integer totalPages, Integer totalResults, List<ExternalMovie> movies) {}
