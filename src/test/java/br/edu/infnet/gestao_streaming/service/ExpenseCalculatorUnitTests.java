package br.edu.infnet.gestao_streaming.service;

import static org.assertj.core.api.Assertions.assertThat;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.domain.model.SubscriptionStatus;
import br.edu.infnet.gestao_streaming.domain.strategy.AnnualExpenseStrategy;
import br.edu.infnet.gestao_streaming.domain.strategy.MonthlyExpenseStrategy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExpenseCalculatorUnitTests {

  private final ExpenseCalculator calculator =
      new ExpenseCalculator(List.of(new MonthlyExpenseStrategy(), new AnnualExpenseStrategy()));

  @Test
  void calculatesMonthlyAndAnnualTotalsIgnoringCancelledSubscriptions() {
    List<Subscription> subscriptions =
        List.of(
            subscription(
                1L, BigDecimal.valueOf(29.90), BillingCycle.MENSAL, SubscriptionStatus.ATIVA),
            subscription(
                2L, BigDecimal.valueOf(120.00), BillingCycle.ANUAL, SubscriptionStatus.ATIVA),
            subscription(
                3L, BigDecimal.valueOf(99.90), BillingCycle.MENSAL, SubscriptionStatus.CANCELADA));

    assertThat(calculator.monthlyTotal(subscriptions)).isEqualByComparingTo("39.90");
    assertThat(calculator.annualTotal(subscriptions)).isEqualByComparingTo("478.80");
  }

  private Subscription subscription(
      Long id, BigDecimal amount, BillingCycle billingCycle, SubscriptionStatus status) {
    return new Subscription(id, 10L, 1L, amount, billingCycle, LocalDate.of(2026, 6, 20), status);
  }
}
