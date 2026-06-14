package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.model.BillingCycle;
import br.edu.infnet.gestao_streaming.model.Subscription;

import java.math.BigDecimal;

public interface BillingCycleExpenseStrategy {

	boolean supports(BillingCycle billingCycle);

	BigDecimal monthlyAmount(Subscription subscription);

	BigDecimal annualAmount(Subscription subscription);
}
