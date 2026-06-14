package br.edu.infnet.gestao_streaming.finance.application;

import br.edu.infnet.gestao_streaming.finance.domain.ExpenseCalculator;
import br.edu.infnet.gestao_streaming.subscription.domain.Subscription;
import br.edu.infnet.gestao_streaming.subscription.domain.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class FinanceFacade {

	private final SubscriptionRepository subscriptions;
	private final ExpenseCalculator calculator;

	public FinanceFacade(SubscriptionRepository subscriptions, ExpenseCalculator calculator) {
		this.subscriptions = subscriptions;
		this.calculator = calculator;
	}

	public ExpenseSummaryResponse summarize(Long userId) {
		List<Subscription> userSubscriptions = subscriptions.findByUserId(userId);
		return new ExpenseSummaryResponse(
				userId,
				scale(calculator.monthlyTotal(userSubscriptions)),
				scale(calculator.annualTotal(userSubscriptions)));
	}

	private BigDecimal scale(BigDecimal amount) {
		return amount.setScale(2, RoundingMode.HALF_UP);
	}
}
