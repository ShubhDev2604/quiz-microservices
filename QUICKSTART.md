# Quick Start Guide

## Prerequisites
- Java 21 installed
- Maven 3.6+ installed
- Docker and Docker Compose installed (for Docker option)
- PostgreSQL installed (for local development option)

## Option 1: Run with Docker Compose (Recommended)

This is the easiest way to run everything:

```bash
# From the project root directory
docker-compose up --build
```

This will:
1. Build all services
2. Start PostgreSQL databases
3. Start Zipkin
4. Start Eureka Service Registry
5. Start Question Service
6. Start Quiz Service
6. Start API Gateway

**Access Points:**
- Eureka Dashboard: http://localhost:8761
- Zipkin UI: http://localhost:9411
- API Gateway: http://localhost:8765
- Quiz Service: http://localhost:8090
- Question Service: http://localhost:8080

**To stop everything:**
```bash
docker-compose down
```

**To stop and remove volumes (clean slate):**
```bash
docker-compose down -v
```

## Option 2: Run Locally (Without Docker)

### Step 1: Start PostgreSQL Databases

You need two PostgreSQL databases running:

**Database 1 (Quiz Service):**
- Port: 5432
- Database: `quizDb`
- Username: `postgres`
- Password: `Admin@1234`

**Database 2 (Question Service):**
- Port: 5433 (or use a different port)
- Database: `questionDb`
- Username: `postgres`
- Password: `Admin@1234`

Create the databases:
```sql
CREATE DATABASE quizDb;
CREATE DATABASE questionDb;
```

### Step 2: Start Zipkin (Optional but recommended)

```bash
# Using Docker
docker run -d -p 9411:9411 openzipkin/zipkin

# Or download and run Zipkin jar
java -jar zipkin.jar
```

### Step 3: Start Services in Order

Open 4 terminal windows:

**Terminal 1 - Service Registry:**
```bash
cd service-registry
mvn spring-boot:run
```
Wait for: "Started ServiceRegistryApplication"

**Terminal 2 - Question Service:**
```bash
cd question-service
mvn spring-boot:run
```
Wait for: "Started QuestionServiceApplication"

**Terminal 3 - Quiz Service:**
```bash
cd quiz-service
mvn spring-boot:run
```
Wait for: "Started QuizServiceApplication"

**Terminal 4 - API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
```
Wait for: "Started ApiGatewayApplication"

## Verify Everything is Working

### 1. Check Eureka Dashboard
Open http://localhost:8761
- You should see: `QUIZ-SERVICE`, `QUESTION-SERVICE`, and `API-GATEWAY` registered

### 2. Check Health Endpoints
```bash
# Quiz Service
curl http://localhost:8090/actuator/health

# Question Service
curl http://localhost:8080/actuator/health

# API Gateway
curl http://localhost:8765/actuator/health

# Service Registry
curl http://localhost:8761/actuator/health
```

### 3. Check Prometheus Metrics
```bash
curl http://localhost:8090/actuator/prometheus
```

### 4. Check Zipkin Tracing
1. Open http://localhost:9411
2. Make a request through the API Gateway
3. Click "Run Query" in Zipkin to see traces

## Test the Circuit Breaker

1. Make sure all services are running
2. Stop the Question Service (Ctrl+C in its terminal)
3. Make a request to Quiz Service that calls Question Service
4. You should see fallback responses being returned
5. Check logs for circuit breaker state changes

## View Logs with Trace IDs

All logs now include `[traceId,spanId]` in the format:
```
2025-02-12 10:30:45.123 [http-nio-8090-exec-1] INFO  [abc123def456,789xyz] in.lifehive.quiz_service.controller.QuizController - Request received
```

You can use the trace ID to correlate logs across all services in Zipkin.

## Troubleshooting

### Port Already in Use
If you get "port already in use" errors:
- Check what's running: `netstat -ano | findstr :8090` (Windows) or `lsof -i :8090` (Mac/Linux)
- Kill the process or change the port in `application.yml`

### Services Not Registering with Eureka
- Make sure Service Registry starts first
- Check that Eureka URL is correct in `application.yml`
- Wait a few seconds for registration

### Database Connection Issues
- Verify PostgreSQL is running
- Check database names match: `quizDb` and `questionDb`
- Verify credentials: `postgres` / `Admin@1234`

### Docker Issues
- Make sure Docker Desktop is running
- Check logs: `docker-compose logs [service-name]`
- Rebuild: `docker-compose up --build --force-recreate`
