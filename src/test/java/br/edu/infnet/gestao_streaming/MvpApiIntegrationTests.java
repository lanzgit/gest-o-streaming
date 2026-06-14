package br.edu.infnet.gestao_streaming;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MvpApiIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void userCanRegisterSubscriptionsAndSeeExpenseSummary() throws Exception {
		mockMvc.perform(post("/streaming-services")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "Netflix",
						  "category": "VIDEO"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("Netflix"))
				.andExpect(jsonPath("$.category").value("VIDEO"));

		mockMvc.perform(post("/streaming-services")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "Spotify",
						  "category": "MUSIC"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(2));

		mockMvc.perform(get("/streaming-services"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));

		mockMvc.perform(post("/users/10/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "streamingServiceId": 1,
						  "amount": 29.90,
						  "billingCycle": "MENSAL",
						  "billingDate": "2026-06-20"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value(10))
				.andExpect(jsonPath("$.status").value("ATIVA"));

		mockMvc.perform(post("/users/10/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "streamingServiceId": 2,
						  "amount": 120.00,
						  "billingCycle": "ANUAL",
						  "billingDate": "2026-07-05"
						}
						"""))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/users/10/subscriptions"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));

		mockMvc.perform(get("/users/10/expenses/summary"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(10))
				.andExpect(jsonPath("$.monthlyTotal").value(39.90))
				.andExpect(jsonPath("$.annualTotal").value(478.80));
	}

	@Test
	void serviceNameIsRequired() throws Exception {
		mockMvc.perform(post("/streaming-services")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "",
						  "category": "VIDEO"
						}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void subscriptionAmountMustBePositive() throws Exception {
		mockMvc.perform(post("/users/10/subscriptions")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "streamingServiceId": 1,
						  "amount": 0,
						  "billingCycle": "MENSAL",
						  "billingDate": "2026-06-20"
						}
						"""))
				.andExpect(status().isBadRequest());
	}
}
