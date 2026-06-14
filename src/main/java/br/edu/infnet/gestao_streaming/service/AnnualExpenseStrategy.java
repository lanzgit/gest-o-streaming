package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Subscription;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
class AnnualExpenseStrategy implements BillingCycleExpenseStrategy {

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
