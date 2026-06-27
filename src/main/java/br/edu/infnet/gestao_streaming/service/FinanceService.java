package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.model.ExpenseSummary;
import br.edu.infnet.gestao_streaming.domain.model.Subscription;
import br.edu.infnet.gestao_streaming.repository.SubscriptionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceService {

  private final SubscriptionRepository subscriptions;
  private final ExpenseCalculator calculator;

  public ExpenseSummary summarize(Long userId) {
    List<Subscription> userSubscriptions = subscriptions.findByUserIdOrderById(userId);
    return new ExpenseSummary(
        userId,
        scale(calculator.monthlyTotal(userSubscriptions)),
        scale(calculator.annualTotal(userSubscriptions)));
  }

  private BigDecimal scale(BigDecimal amount) {
    return amount.setScale(2, RoundingMode.HALF_UP);
  }
}
