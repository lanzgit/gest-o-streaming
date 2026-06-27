package br.edu.infnet.gestao_streaming;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
  void userCanCancelSubscriptionAndRemoveItFromExpensesAndUpcomingBilling() throws Exception {
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
        .andExpect(jsonPath("$.id").value(1));

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
						  "billingDate": "2026-06-25"
						}
						"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("ATIVA"));

    mockMvc
        .perform(patch("/users/10/subscriptions/1/cancel"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("CANCELADA"));

    mockMvc
        .perform(get("/users/10/subscriptions"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].status").value("CANCELADA"));

    mockMvc
        .perform(get("/users/10/expenses/summary"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.monthlyTotal").value(0))
        .andExpect(jsonPath("$.annualTotal").value(0));

    mockMvc
        .perform(get("/users/10/billing/upcoming?days=7"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void cannotCancelSubscriptionFromAnotherUserOrAlreadyCancelledSubscription() throws Exception {
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
        .andExpect(status().isCreated());

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
						  "billingDate": "2026-06-25"
						}
						"""))
        .andExpect(status().isCreated());

    mockMvc
        .perform(patch("/users/99/subscriptions/1/cancel"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("Subscription not found for user."));

    mockMvc.perform(patch("/users/10/subscriptions/1/cancel")).andExpect(status().isOk());

    mockMvc
        .perform(patch("/users/10/subscriptions/1/cancel"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("Subscription is already cancelled."));
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

  @Test
  void userCanRegisterAndListSubscriptionPayments() throws Exception {
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
        .andExpect(jsonPath("$.id").value(1));

    mockMvc
        .perform(
            post("/users/77/subscriptions")
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
        .andExpect(jsonPath("$.id").value(1));

    mockMvc
        .perform(
            post("/users/77/subscriptions/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "amount": 29.90,
						  "paidAt": "2026-06-20"
						}
						"""))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.userId").value(77))
        .andExpect(jsonPath("$.subscriptionId").value(1))
        .andExpect(jsonPath("$.amount").value(29.90))
        .andExpect(jsonPath("$.paidAt").value("2026-06-20"))
        .andExpect(jsonPath("$.status").value("CONFIRMADO"));

    mockMvc
        .perform(get("/users/77/payments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].subscriptionId").value(1));

    mockMvc
        .perform(get("/users/77/subscriptions/1/payments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void paymentAmountMustBePositive() throws Exception {
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
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/users/77/subscriptions")
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
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/users/77/subscriptions/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "amount": 0,
						  "paidAt": "2026-06-20"
						}
						"""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void userCanClassifyAndSummarizeSubscriptionUsage() throws Exception {
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
        .andExpect(status().isCreated());

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
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/users/88/subscriptions")
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
        .andExpect(jsonPath("$.id").value(1));

    mockMvc
        .perform(
            post("/users/88/subscriptions")
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
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2));

    mockMvc
        .perform(
            patch("/users/88/subscriptions/1/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "level": "RARO"
						}
						"""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.subscriptionId").value(1))
        .andExpect(jsonPath("$.level").value("RARO"));

    mockMvc
        .perform(
            patch("/users/88/subscriptions/2/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "level": "FREQUENTE"
						}
						"""))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            patch("/users/88/subscriptions/1/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "level": "NAO_USADO"
						}
						"""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.level").value("NAO_USADO"));

    mockMvc
        .perform(get("/users/88/subscriptions/usage"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    mockMvc
        .perform(get("/users/88/subscriptions/usage/low"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].subscriptionId").value(1));

    mockMvc
        .perform(get("/users/88/subscriptions/usage-summary"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.frequentCount").value(1))
        .andExpect(jsonPath("$.rareCount").value(0))
        .andExpect(jsonPath("$.notUsedCount").value(1));
  }

  @Test
  void usageLevelIsRequired() throws Exception {
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
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/users/88/subscriptions")
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
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            patch("/users/88/subscriptions/1/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "level": null
						}
						"""))
        .andExpect(status().isBadRequest());
  }

  @Test
  void userCanSeeConsolidatedDashboard() throws Exception {
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
        .andExpect(status().isCreated());

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
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/users/55/subscriptions")
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
        .andExpect(jsonPath("$.id").value(1));

    mockMvc
        .perform(
            post("/users/55/subscriptions")
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
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(2));

    mockMvc
        .perform(
            patch("/users/55/subscriptions/1/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "level": "NAO_USADO"
						}
						"""))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            patch("/users/55/subscriptions/2/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "level": "FREQUENTE"
						}
						"""))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/users/55/subscriptions/1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
						{
						  "amount": 29.90,
						  "paidAt": "2026-06-20"
						}
						"""))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/users/55/notifications/generate?days=20")).andExpect(status().isOk());

    mockMvc
        .perform(get("/users/55/dashboard?billingWindowDays=20&recentPaymentsLimit=3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(55))
        .andExpect(jsonPath("$.expenses.monthlyTotal").value(39.90))
        .andExpect(jsonPath("$.expenses.annualTotal").value(478.80))
        .andExpect(jsonPath("$.activeSubscriptionsCount").value(2))
        .andExpect(jsonPath("$.cancelledSubscriptionsCount").value(0))
        .andExpect(jsonPath("$.lowUsageSubscriptionsCount").value(1))
        .andExpect(jsonPath("$.unreadNotificationsCount").value(2))
        .andExpect(jsonPath("$.usage.frequentCount").value(1))
        .andExpect(jsonPath("$.usage.notUsedCount").value(1))
        .andExpect(jsonPath("$.upcomingBillings", hasSize(2)))
        .andExpect(jsonPath("$.recentPayments", hasSize(1)))
        .andExpect(jsonPath("$.recentPayments[0].subscriptionId").value(1));
  }
}
