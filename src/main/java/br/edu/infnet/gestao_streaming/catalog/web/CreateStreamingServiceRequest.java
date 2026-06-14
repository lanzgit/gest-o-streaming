package br.edu.infnet.gestao_streaming.catalog.web;

import br.edu.infnet.gestao_streaming.catalog.application.CreateStreamingServiceCommand;

record CreateStreamingServiceRequest(String name, String category) {

	CreateStreamingServiceCommand toCommand() {
		return new CreateStreamingServiceCommand(name, category);
	}
}
