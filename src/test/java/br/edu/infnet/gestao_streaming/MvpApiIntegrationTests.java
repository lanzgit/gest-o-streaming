package br.edu.infnet.gestao_streaming;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MvpApiIntegrationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  void userCanRegisterSubscriptionsAndSeeExpenseSummary() throws Exception {
    mockMvc
        .perform(
            post("/streaming-services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "name": "Netflix",
						  "category": "VIDEO"
						}
						"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Netflix"))
        .andExpect(jsonPath("$.category").value("VIDEO"));

    mockMvc
        .perform(
            post("/streaming-services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "name": "Spotify",
						  "category": "MUSIC"
						}
						"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2));

    mockMvc
        .perform(get("/streaming-services"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    mockMvc
        .perform(
            post("/users/10/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
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

    mockMvc
        .perform(
            post("/users/10/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "streamingServiceId": 2,
						  "amount": 120.00,
						  "billingCycle": "ANUAL",
						  "billingDate": "2026-07-05"
						}
						"""))
        .andExpect(status().isCreated());

    mockMvc
        .perform(get("/users/10/subscriptions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    mockMvc
        .perform(get("/users/10/expenses/summary"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(10))
        .andExpect(jsonPath("$.monthlyTotal").value(39.90))
        .andExpect(jsonPath("$.annualTotal").value(478.80));
  }

  @Test
  void serviceNameIsRequired() throws Exception {
    mockMvc
        .perform(
            post("/streaming-services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "name": "",
						  "category": "VIDEO"
						}
						"""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void subscriptionAmountMustBePositive() throws Exception {
    mockMvc
        .perform(
            post("/users/10/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "streamingServiceId": 1,
						  "amount": 0,
						  "billingCycle": "MENSAL",
						  "billingDate": "2026-06-20"
						}
						"""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void userCanGenerateNotificationsForUpcomingBilling() throws Exception {
    mockMvc
        .perform(
            post("/streaming-services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "name": "Prime Video",
						  "category": "VIDEO"
						}
						"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));

    mockMvc
        .perform(
            post("/users/99/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "streamingServiceId": 1,
						  "amount": 19.90,
						  "billingCycle": "MENSAL",
						  "billingDate": "2026-06-25"
						}
						"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));

    mockMvc
        .perform(get("/users/99/billing/upcoming?days=7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].subscriptionId").value(1))
        .andExpect(jsonPath("$[0].dueDate").value("2026-06-25"))
        .andExpect(jsonPath("$[0].daysUntilDue").value(5));

    mockMvc
        .perform(post("/users/99/notifications/generate?days=7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].subscriptionId").value(1))
        .andExpect(jsonPath("$[0].dueDate").value("2026-06-25"))
        .andExpect(jsonPath("$[0].status").value("NAO_LIDA"));

    mockMvc
        .perform(post("/users/99/notifications/generate?days=7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1));

    mockMvc
        .perform(get("/users/99/notifications"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}
