package br.edu.infnet.gestao_streaming.domain.strategy;

import br.edu.infnet.gestao_streaming.domain.model.BillingCycle;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import java.math.BigDecimal;

public interface BillingCycleExpenseStrategy {

  boolean supports(BillingCycle billingCycle);

  BigDecimal monthlyAmount(Subscription subscription);

  BigDecimal annualAmount(Subscription subscription);
}
