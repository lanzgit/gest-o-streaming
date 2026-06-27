package br.edu.infnet.gestao_streaming.domain.factory;

public interface DomainFactory<I, O> {

  O create(I input);
}
