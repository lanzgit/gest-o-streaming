package br.edu.infnet.gestao_streaming.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long streamingServiceId;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private BillingCycle billingCycle;

	@Column(nullable = false)
	private LocalDate billingDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private SubscriptionStatus status;

	protected Subscription() {
	}

	public Subscription(
			Long id,
			Long userId,
			Long streamingServiceId,
			BigDecimal amount,
			BillingCycle billingCycle,
			LocalDate billingDate,
			SubscriptionStatus status) {
		this.id = id;
		this.userId = userId;
		this.streamingServiceId = streamingServiceId;
		this.amount = amount;
		this.billingCycle = billingCycle;
		this.billingDate = billingDate;
		this.status = status;
	}

	public Long id() {
		return id;
	}

	public Long userId() {
		return userId;
	}

	public Long streamingServiceId() {
		return streamingServiceId;
	}

	public BigDecimal amount() {
		return amount;
	}

	public BillingCycle billingCycle() {
		return billingCycle;
	}

	public LocalDate billingDate() {
		return billingDate;
	}

	public SubscriptionStatus status() {
		return status;
	}

	public boolean isActive() {
		return status == SubscriptionStatus.ATIVA;
	}
}
