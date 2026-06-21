package br.edu.infnet.gestao_streaming.service;

import br.edu.infnet.gestao_streaming.domain.model.Notification;
import br.edu.infnet.gestao_streaming.domain.model.NotificationStatus;
import br.edu.infnet.gestao_streaming.domain.model.UpcomingBilling;
import br.edu.infnet.gestao_streaming.repository.NotificationRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final BillingService billingService;
  private final NotificationRepository notifications;
  private final Clock clock;

  public List<Notification> generate(Long userId, Integer days) {
    return billingService.listUpcoming(userId, days).stream().map(this::findOrCreate).toList();
  }

  public List<Notification> listByUser(Long userId) {
    return notifications.findByUserId(userId);
  }

  private Notification findOrCreate(UpcomingBilling billing) {
    return notifications
        .findBySubscriptionIdAndDueDate(billing.subscriptionId(), billing.dueDate())
        .orElseGet(() -> notifications.save(createNotification(billing)));
  }

  private Notification createNotification(UpcomingBilling billing) {
    return new Notification(
        null,
        billing.userId(),
        billing.subscriptionId(),
        billing.dueDate(),
        "Cobranca da assinatura " + billing.subscriptionId() + " vence em " + billing.dueDate(),
        NotificationStatus.NAO_LIDA,
        LocalDateTime.now(clock));
  }
}
