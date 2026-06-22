package br.edu.infnet.gestao_streaming.domain.model;

import java.util.List;

public record DashboardSummary(
    Long userId,
    ExpenseSummary expenses,
    UsageSummary usage,
    long activeSubscriptionsCount,
    long cancelledSubscriptionsCount,
    long lowUsageSubscriptionsCount,
    long unreadNotificationsCount,
    List<UpcomingBilling> upcomingBillings,
    List<Payment> recentPayments) {}
