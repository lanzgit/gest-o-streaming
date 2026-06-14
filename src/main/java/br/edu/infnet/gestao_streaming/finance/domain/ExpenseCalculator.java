package br.edu.infnet.gestao_streaming.finance.domain;

import br.edu.infnet.gestao_streaming.subscription.domain.Subscription;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ExpenseCalculator {

	private final List<BillingCycleExpenseStrategy> strategies;

	public ExpenseCalculator(List<BillingCycleExpenseStrategy> strategies) {
		this.strategies = strategies;
	}

	public BigDecimal monthlyTotal(List<Subscription> subscriptions) {
		return subscriptions.stream()
				.filter(Subscription::isActive)
				.map(this::monthlyAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal annualTotal(List<Subscription> subscriptions) {
		return subscriptions.stream()
				.filter(Subscription::isActive)
				.map(this::annualAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal monthlyAmount(Subscription subscription) {
		return strategyFor(subscription).monthlyAmount(subscription);
	}

	private BigDecimal annualAmount(Subscription subscription) {
		return strategyFor(subscription).annualAmount(subscription);
	}

	private BillingCycleExpenseStrategy strategyFor(Subscription subscription) {
		return strategies.stream()
				.filter(strategy -> strategy.supports(subscription.billingCycle()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unsupported billing cycle."));
	}
}
