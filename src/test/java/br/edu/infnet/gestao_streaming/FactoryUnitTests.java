package br.edu.infnet.gestao_streaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.StreamingService;
import br.edu.infnet.gestao_streaming.model.Subscription;
import br.edu.infnet.gestao_streaming.model.SubscriptionDraft;
import br.edu.infnet.gestao_streaming.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.service.StreamingServiceFactory;
import br.edu.infnet.gestao_streaming.service.SubscriptionFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class FactoryUnitTests {

  private final StreamingServiceFactory streamingServiceFactory = new StreamingServiceFactory();
  private final SubscriptionFactory subscriptionFactory = new SubscriptionFactory();

  @Test
  void streamingServiceFactoryTrimsValidInput() {
    StreamingService service = streamingServiceFactory.create(" Netflix ", " VIDEO ");

    assertThat(service.id()).isNull();
    assertThat(service.name()).isEqualTo("Netflix");
    assertThat(service.category()).isEqualTo("VIDEO");
  }

  @Test
  void streamingServiceFactoryRequiresNameAndCategory() {
    assertThatThrownBy(() -> streamingServiceFactory.create("", "VIDEO"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Service name is required.");

    assertThatThrownBy(() -> streamingServiceFactory.create("Netflix", " "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Service category is required.");
  }

  @Test
  void subscriptionFactoryCreatesActiveSubscription() {
    Subscription subscription =
        subscriptionFactory.create(
            new SubscriptionDraft(
                10L,
                1L,
                BigDecimal.valueOf(29.90),
                BillingCycle.MENSAL,
                LocalDate.of(2026, 6, 20)));

    assertThat(subscription.id()).isNull();
    assertThat(subscription.userId()).isEqualTo(10L);
    assertThat(subscription.streamingServiceId()).isEqualTo(1L);
    assertThat(subscription.status()).isEqualTo(SubscriptionStatus.ATIVA);
  }

  @Test
  void subscriptionFactoryRequiresPositiveAmount() {
    SubscriptionDraft draft =
        new SubscriptionDraft(
            10L, 1L, BigDecimal.ZERO, BillingCycle.MENSAL, LocalDate.of(2026, 6, 20));

    assertThatThrownBy(() -> subscriptionFactory.create(draft))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Subscription amount must be positive.");
  }
}
