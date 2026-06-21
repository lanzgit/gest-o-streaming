CREATE TABLE IF NOT EXISTS streaming_services (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    category VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    streaming_service_id BIGINT NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    billing_cycle VARCHAR(20) NOT NULL,
    billing_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_subscriptions_streaming_service
        FOREIGN KEY (streaming_service_id)
        REFERENCES streaming_services (id)
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    due_date DATE NOT NULL,
    message VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_notifications_subscription
        FOREIGN KEY (subscription_id)
        REFERENCES subscriptions (id),
    CONSTRAINT uk_notifications_subscription_due_date
        UNIQUE (subscription_id, due_date)
);

INSERT INTO streaming_services (id, name, category) VALUES
    (1, 'Netflix', 'VIDEO'),
    (2, 'Spotify', 'MUSIC'),
    (3, 'Prime Video', 'VIDEO'),
    (4, 'Disney+', 'VIDEO'),
    (5, 'Max', 'VIDEO'),
    (6, 'Crunchyroll', 'ANIME')
ON CONFLICT (id) DO NOTHING;

INSERT INTO subscriptions (
    id,
    user_id,
    streaming_service_id,
    amount,
    billing_cycle,
    billing_date,
    status
) VALUES
    (1, 10, 1, 29.90, 'MENSAL', '2026-06-20', 'ATIVA'),
    (2, 10, 2, 120.00, 'ANUAL', '2026-07-05', 'ATIVA'),
    (3, 10, 3, 19.90, 'MENSAL', '2026-06-28', 'ATIVA'),
    (4, 20, 4, 27.90, 'MENSAL', '2026-06-18', 'ATIVA'),
    (5, 20, 5, 239.90, 'ANUAL', '2026-08-01', 'ATIVA'),
    (6, 30, 6, 14.99, 'MENSAL', '2026-06-25', 'CANCELADA')
ON CONFLICT (id) DO NOTHING;

SELECT setval(
    pg_get_serial_sequence('streaming_services', 'id'),
    COALESCE((SELECT MAX(id) FROM streaming_services), 1)
);

SELECT setval(
    pg_get_serial_sequence('subscriptions', 'id'),
    COALESCE((SELECT MAX(id) FROM subscriptions), 1)
);

SELECT setval(
    pg_get_serial_sequence('notifications', 'id'),
    COALESCE((SELECT MAX(id) FROM notifications), 1)
);
