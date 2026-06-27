package br.edu.infnet.gestao_streaming.domain.factory;

import br.edu.infnet.gestao_streaming.domain.command.CreateStreamingServiceCommand;
import br.edu.infnet.gestao_streaming.domain.model.StreamingService;

public interface StreamingServiceCreator
    extends DomainFactory<CreateStreamingServiceCommand, StreamingService> {}
