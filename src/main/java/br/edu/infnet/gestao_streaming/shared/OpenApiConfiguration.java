package br.edu.infnet.gestao_streaming.shared;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfiguration {

	@Bean
	OpenAPI streamingManagementOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("API de Gestao de Assinaturas de Streaming")
						.version("v1")
						.description("MVP para cadastro de servicos, assinaturas e resumo financeiro."));
	}
}
