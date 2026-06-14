package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Subscription;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
class MonthlyExpenseStrategy implements BillingCycleExpenseStrategy {

  @Override
  public boolean supports(BillingCycle billingCycle) {
    return billingCycle == BillingCycle.MENSAL;
  }

  @Override
  public BigDecimal monthlyAmount(Subscription subscription) {
    return subscription.amount();
  }

  @Override
  public BigDecimal annualAmount(Subscription subscription) {
    return subscription.amount().multiply(BigDecimal.valueOf(12));
  }
}
