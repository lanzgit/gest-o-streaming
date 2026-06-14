package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Subscription;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
