package br.edu.infnet.gestao_streaming.finance.domain;

import br.edu.infnet.gestao_streaming.subscription.domain.BillingCycle;
import br.edu.infnet.gestao_streaming.subscription.domain.Subscription;

import java.math.BigDecimal;

public interface BillingCycleExpenseStrategy {

	boolean supports(BillingCycle billingCycle);

	BigDecimal monthlyAmount(Subscription subscription);

	BigDecimal annualAmount(Subscription subscription);
}
