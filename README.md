# Gestao Streaming

API REST para gestao de assinaturas de streaming. O projeto ajuda o usuario a cadastrar e cancelar assinaturas, acompanhar gastos, controlar vencimentos, registrar pagamentos simulados, classificar uso e consultar um dashboard consolidado.

## Objetivo

Dar visibilidade financeira sobre servicos recorrentes, como Netflix, Spotify, Prime Video, Disney+ e outros. A aplicacao foi desenvolvida como projeto educacional, com foco em arquitetura em camadas, padroes de projeto, testes automatizados, documentacao OpenAPI e execucao via Docker.

## Funcionalidades

- Cadastro e listagem de servicos de streaming.
- Cadastro, listagem e cancelamento de assinaturas por usuario.
- Calculo de gasto mensal e anual.
- Consulta de proximos vencimentos.
- Geracao e listagem de notificacoes simuladas.
- Registro e consulta de historico de pagamentos.
- Classificacao de uso: `FREQUENTE`, `RARO` e `NAO_USADO`.
- Dashboard consolidado por usuario.
- Integracao externa com TMDB para catalogo de provedores e filmes por streaming.
- Documentacao Swagger/OpenAPI.
- Banco PostgreSQL com seed SQL.
- Colecao Insomnia para testes manuais.

## Tecnologias

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA
- PostgreSQL 17 Alpine
- Docker e Docker Compose
- Springdoc OpenAPI/Swagger UI
- Lombok
- JUnit
- Spotify Java Formatter

## Arquitetura

O projeto segue uma organizacao em camadas com inspiracao MVC:

```text
src/main/java/br/edu/infnet/gestao_streaming
|-- config          # Configuracoes da aplicacao
|-- controller      # Camada HTTP/API REST
|-- domain
|   |-- command     # Objetos de comando para casos de uso
|   |-- factory     # Contratos e creators concretos para criacao de entidades
|   |-- model       # Entidades, enums e modelos de dominio
|   `-- strategy    # Estrategias de calculo financeiro
|-- dto             # Objetos de entrada e saida da API
|-- external/tmdb   # Integracao com API externa TMDB
|-- repository      # Interfaces Spring Data JPA de persistencia
`-- service         # Casos de uso e regras de aplicacao
```

A camada `controller` recebe requisicoes HTTP e converte respostas para DTOs. A camada `service` coordena casos de uso. O pacote `domain` concentra regras e objetos do dominio. A camada `repository` usa Spring Data JPA para persistencia. A pasta `external/tmdb` isola a integracao com a API externa.

## Como Subir Com Docker

Na raiz do projeto, execute:

```bash
docker compose up --build
```

A API ficara disponivel em:

```text
http://localhost:8080
```

O PostgreSQL ficara disponivel em:

```text
localhost:5432
```

Credenciais padrao do banco:

```text
database: gestao_streaming
user: gestao
password: gestao
```

Se voce alterou o script SQL inicial ou quer recriar o banco do zero:

```bash
docker compose down -v
docker compose up --build
```

## Como Rodar Localmente

Suba apenas o banco:

```bash
docker compose up db
```

Em outro terminal, rode a aplicacao:

```bash
./mvnw spring-boot:run
```

## Documentacao Da API

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Endpoints Principais

- `POST /streaming-services`
- `GET /streaming-services`
- `POST /users/{userId}/subscriptions`
- `GET /users/{userId}/subscriptions`
- `PATCH /users/{userId}/subscriptions/{subscriptionId}/cancel`
- `GET /users/{userId}/expenses/summary`
- `GET /users/{userId}/billing/upcoming`
- `POST /users/{userId}/notifications/generate`
- `GET /users/{userId}/notifications`
- `POST /users/{userId}/subscriptions/{subscriptionId}/payments`
- `GET /users/{userId}/payments`
- `PATCH /users/{userId}/subscriptions/{subscriptionId}/usage`
- `GET /users/{userId}/dashboard`

## Cancelamento De Assinatura

```http
PATCH http://localhost:8080/users/10/subscriptions/1/cancel
```

Resposta resumida:

```json
{
  "id": 1,
  "userId": 10,
  "streamingServiceId": 1,
  "amount": 29.90,
  "billingCycle": "MENSAL",
  "billingDate": "2026-06-25",
  "status": "CANCELADA"
}
```

Assinaturas canceladas continuam aparecendo na listagem do usuario, mas deixam de compor gastos ativos, proximos vencimentos e contagem de assinaturas ativas no dashboard.

## Exemplo De Dashboard

```http
GET http://localhost:8080/users/10/dashboard?billingWindowDays=7&recentPaymentsLimit=5
```

Resposta resumida:

```json
{
  "userId": 10,
  "expenses": {
    "userId": 10,
    "monthlyTotal": 39.90,
    "annualTotal": 478.80
  },
  "usage": {
    "frequentCount": 1,
    "rareCount": 1,
    "notUsedCount": 1
  },
  "activeSubscriptionsCount": 3,
  "cancelledSubscriptionsCount": 0,
  "lowUsageSubscriptionsCount": 2,
  "unreadNotificationsCount": 0,
  "upcomingBillings": [],
  "recentPayments": []
}
```

## Insomnia

O arquivo `insomnia-gestao-streaming.json` pode ser importado no Insomnia para testar a API manualmente.

Variaveis principais do ambiente:

- `base_url`: `http://localhost:8080`
- `user_id`: usuario usado nos testes manuais
- `tmdb_region`: regiao usada na API TMDB, por exemplo `BR`
- `tmdb_provider_id`: provedor TMDB, por exemplo `8`

## Banco De Dados

O script de criacao e carga inicial fica em:

```text
docker/postgres/init/01-schema-and-seed.sql
```

Ele cria e popula tabelas como:

- `streaming_services`
- `subscriptions`
- `notifications`
- `payments`
- `subscription_usage`

## Testes

Para executar a suite:

```bash
./mvnw test
```

Para verificar formatacao:

```bash
./mvnw fmt:check
```

Para formatar o codigo:

```bash
./mvnw fmt:format
```

## Hook

O projeto possui hook em:

```text
.githooks/pre-commit
```

Ele executa o Spotify Java Formatter antes do commit. Para habilitar:

```bash
git config core.hooksPath .githooks
```

## Variaveis De Ambiente

Principais variaveis aceitas:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_JPA_HIBERNATE_DDL_AUTO
TMDB_BASE_URL
TMDB_API_TOKEN
TMDB_DEFAULT_REGION
TMDB_DEFAULT_LANGUAGE
```

O token TMDB possui valor padrao no projeto porque a aplicacao tem finalidade educacional. Em um projeto real, esse valor deveria ficar fora do codigo, usando secret manager ou variaveis de ambiente.

## Padroes De Projeto

O projeto aplica Factory Method, Adapter, Repository, Strategy e Facade, alem de principios SOLID em pontos importantes da arquitetura.
