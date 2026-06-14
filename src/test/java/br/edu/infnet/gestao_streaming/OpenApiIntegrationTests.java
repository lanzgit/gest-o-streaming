package br.edu.infnet.gestao_streaming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void exposesOpenApiContractForMvpEndpoints() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title").value("API de Gestao de Assinaturas de Streaming"))
				.andExpect(jsonPath("$.paths['/streaming-services']").exists())
				.andExpect(jsonPath("$.paths['/users/{userId}/subscriptions']").exists())
				.andExpect(jsonPath("$.paths['/users/{userId}/expenses/summary']").exists());
	}
}
