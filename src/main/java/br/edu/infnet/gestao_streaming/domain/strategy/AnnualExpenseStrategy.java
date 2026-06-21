package br.edu.infnet.gestao_streaming.domain.strategy;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class AnnualExpenseStrategy implements BillingCycleExpenseStrategy {

  @Override
  public boolean supports(BillingCycle billingCycle) {
    return billingCycle == BillingCycle.ANUAL;
  }

  @Override
  public BigDecimal monthlyAmount(Subscription subscription) {
    return subscription.amount().divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
  }

  @Override
  public BigDecimal annualAmount(Subscription subscription) {
    return subscription.amount();
  }
}
