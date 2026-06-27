package br.edu.infnet.gestao_streaming.external.model;

import java.time.LocalDate;

public record ExternalMovie(
    Integer id,
    String title,
    String overview,
    String posterUrl,
    LocalDate releaseDate,
    Double voteAverage,
    Double popularity) {}
