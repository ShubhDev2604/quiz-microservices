# Runbook (for Future You)

This file is intentionally **not recruiter-facing**. It’s the practical “how do I get productive again?” guide for returning to this repo after weeks/months.

## 1) What this project is (30-second refresher)

- A **quiz platform** implemented as Spring Boot microservices.
- Traffic comes through **`api-gateway`**.
- Services register with **Eureka** (**`service-registry`**).
- `quiz-service` calls `question-service` via **Feign**.
- **Resilience4j** protects inter-service calls (circuit breaker + fallback).
- **Zipkin** provides distributed tracing; Actuator exposes metrics/health.

## 2) Services, ports, and URLs (copy/paste reference)

| Component | Host port | URL / notes |
|---|---:|---|
| Eureka | 8761 | `http://localhost:8761` |
| API Gateway | 8765 | `http://localhost:8765` |
| Question Service | 8080 | `http://localhost:8080` |
| Quiz Service | 8090 | `http://localhost:8090` |
| Zipkin | 9411 | `http://localhost:9411` |
| Postgres (quizDb) | 5432 | DB: `quizDb` |
| Postgres (questionDb) | 5433 | DB: `questionDb` |

Default DB credentials used in this repo:

- user: `postgres`
- password: `Admin@1234`

## 3) Fastest way to run (Docker)

From repo root:

```bash
docker-compose up --build
```

Stop:

```bash
docker-compose down
```

Clean slate (drops local Docker volumes, including DB data):

```bash
docker-compose down -v
```

### Confirm it’s healthy

- Check Eureka: `http://localhost:8761` (expect `API-GATEWAY`, `QUIZ-SERVICE`, `QUESTION-SERVICE`)
- Check health endpoints:
  - `http://localhost:8765/actuator/health`
  - `http://localhost:8090/actuator/health`
  - `http://localhost:8080/actuator/health`

## 4) Local run (no Docker)

You need:

- Java 21
- Maven
- Two local Postgres instances (or one instance with two DBs + separate ports)
- Optional: Zipkin

Start services in order:

```bash
cd service-registry && mvn spring-boot:run
cd question-service && mvn spring-boot:run
cd quiz-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

Zipkin (optional):

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

## 5) Where to change what (most common tasks)

### Add / change an API route

- **Gateway routing config**: `api-gateway/src/main/resources/application.yml`
  - Search for route definitions and service IDs used for discovery-based routing.

### Add a new endpoint in Question Service

- Controller: `question-service/src/main/java/in/lifehive/question_service/controller/QuestionController.java`
- If you add new request/response DTOs, keep them in a dedicated package (`dto/` is a good convention).

### Add a new endpoint in Quiz Service

- Controller: `quiz-service/src/main/java/in/lifehive/quiz_service/controller/QuizController.java`
- Feign client(s):
  - Interface: `quiz-service/src/main/java/in/lifehive/quiz_service/feign/QuizInterface.java`
  - Fallback: `quiz-service/src/main/java/in/lifehive/quiz_service/feign/QuizInterfaceFallback.java`

### Update circuit breaker behavior

- Resilience4j config typically lives in: `quiz-service/src/main/resources/application.yml`
- Usual knobs:
  - sliding window size
  - failure threshold
  - open-state wait duration
  - time limiter duration

### Logging format / trace IDs

- Logback config per service:
  - `*/src/main/resources/logback-spring.xml`
- Pattern includes MDC values: `traceId` and `spanId`.

### Docker / compose changes

- Compose orchestration: `docker-compose.yml`
- Service image builds: `*/Dockerfile`
- Docker profile configs:
  - `*/src/main/resources/application-docker.yml`

## 6) Quick “smoke test” checklist (manual)

1. Start everything (Docker is easiest).
2. Add a question:
   - `POST /question/add`
3. Create a quiz:
   - `POST /quiz/create`
4. Fetch quiz questions:
   - `GET /quiz/get/{id}`
5. Submit quiz:
   - `POST /quiz/submit/{id}`
6. Open Zipkin and confirm traces appear:
   - `http://localhost:9411`

Tip: Prefer sending requests through the gateway (`http://localhost:8765/...`) so you validate routing + tracing end-to-end.

## 7) Troubleshooting (things you’ll forget later)

### Nothing registers in Eureka

- Ensure `service-registry` is up first.
- Wait ~30–60 seconds for registration in Docker.
- Confirm services can reach registry:
  - In Docker, service-to-service URLs use container names (compose sets `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`).

### Postgres connection errors

- Confirm ports are free on host: `5432`, `5433`
- If Docker volumes contain old schema/data and you want a reset:
  - `docker-compose down -v`

### Zipkin shows no traces

- Confirm Zipkin is running: `http://localhost:9411`
- Make requests through the gateway (best signal).
- Check each service has Zipkin base URL set under docker profile (`SPRING_ZIPKIN_BASE_URL` is provided via compose).

### Circuit breaker fallback not triggering

- Stop `question-service`, then call an endpoint in `quiz-service` that depends on it.
- Confirm:
  - Feign client has a fallback class wired as a Spring bean
  - Resilience4j is enabled and configured for that downstream call

## 8) Notes for future improvements (optional backlog)

- Add automated tests (controller/service-level, contract tests between services).
- Add OpenAPI/Swagger for each service (and possibly aggregated at gateway).
- Add centralized log shipping (ELK/Loki) if turning this into a “real” deployment.
- Add CI workflow (build + test + docker build).

