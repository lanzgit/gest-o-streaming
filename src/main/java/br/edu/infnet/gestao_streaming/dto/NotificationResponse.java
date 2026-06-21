package br.edu.infnet.gestao_streaming.dto;

import br.edu.infnet.gestao_streaming.domain.model.Notification;
import br.edu.infnet.gestao_streaming.domain.model.NotificationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    Long userId,
    Long subscriptionId,
    LocalDate dueDate,
    String message,
    NotificationStatus status,
    LocalDateTime createdAt) {

  public static NotificationResponse from(Notification notification) {
    return new NotificationResponse(
        notification.id(),
        notification.userId(),
        notification.subscriptionId(),
        notification.dueDate(),
        notification.message(),
        notification.status(),
        notification.createdAt());
  }
}
