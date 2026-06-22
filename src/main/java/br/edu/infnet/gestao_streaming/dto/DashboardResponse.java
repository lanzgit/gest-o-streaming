package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.model.DashboardSummary;
import java.util.List;

public record DashboardResponse(
    Long userId,
    ExpenseSummaryResponse expenses,
    UsageSummaryResponse usage,
    long activeSubscriptionsCount,
    long cancelledSubscriptionsCount,
    long lowUsageSubscriptionsCount,
    long unreadNotificationsCount,
    List<UpcomingBillingResponse> upcomingBillings,
    List<PaymentResponse> recentPayments) {

  public static DashboardResponse from(DashboardSummary summary) {
    return new DashboardResponse(
        summary.userId(),
        ExpenseSummaryResponse.from(summary.expenses()),
        UsageSummaryResponse.from(summary.usage()),
        summary.activeSubscriptionsCount(),
        summary.cancelledSubscriptionsCount(),
        summary.lowUsageSubscriptionsCount(),
        summary.unreadNotificationsCount(),
        summary.upcomingBillings().stream().map(UpcomingBillingResponse::from).toList(),
        summary.recentPayments().stream().map(PaymentResponse::from).toList());
  }
}
