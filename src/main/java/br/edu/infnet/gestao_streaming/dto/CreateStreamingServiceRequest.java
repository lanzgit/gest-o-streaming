package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.command.CreateStreamingServiceCommand;

public record CreateStreamingServiceRequest(String name, String category) {

  public CreateStreamingServiceCommand toCommand() {
    return new CreateStreamingServiceCommand(name, category);
  }
}
