# Notes Cloud Sharing Service

Sharing Service is a Spring Boot microservice in the Notes Cloud platform. It creates temporary share links for notes and allows another user to open a shared note through a secure token.

For now, the service supports view-only sharing.

## Main Idea

The service does not put the `userId` or `noteId` directly inside the public URL. Instead, it generates a random token and stores the relation in the database.

```text
token -> noteId, ownerId, expiresAt
```

When someone opens the link, the service validates the token and expiration time. If the token is valid, it calls Notes Service to fetch the note and returns it.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker
- Kubernetes
- Maven

## API Base URL

Through Gateway / local cluster access:

```text
http://localhost:8090/api/v1
```

Direct Sharing Service local/dev access:

```text
http://localhost:8083
```

Inside Kubernetes:

```text
http://sharing-service:8083
```

## Endpoints

### Create Share Link

Creates a temporary share link for a note.

```http
POST /api/v1/users/{userId}/notes/{noteId}/share-links
```

No request body is required.

Through Gateway example:

```bash
curl -X POST http://localhost:8090/api/v1/users/22222222-2222-2222-2222-222222222222/notes/11111111-1111-1111-1111-111111111111/share-links
```

Direct service example:

```bash
curl -X POST http://localhost:8083/api/v1/users/22222222-2222-2222-2222-222222222222/notes/11111111-1111-1111-1111-111111111111/share-links
```

Example response:

```json
{
  "id": "6f099ecd-9f94-487e-bf2d-b2a64bf2b917",
  "noteId": "11111111-1111-1111-1111-111111111111",
  "url": "http://localhost:8080/shared/4uJnzYqu0jtJithJii_s0WXo9G0Ykvx3dvZas7bcy4E",
  "expiresAt": "2026-05-07T12:05:00"
}
```

### Open Share Link

The user normally opens the public frontend page:

```http
GET /shared/{token}
```

The frontend then calls the gateway API endpoint:

```http
GET /api/v1/share-links/{token}
```

Through Gateway example:

```bash
curl http://localhost:8090/api/v1/share-links/4uJnzYqu0jtJithJii_s0WXo9G0Ykvx3dvZas7bcy4E
```

Direct service example:

```bash
curl http://localhost:8083/api/v1/share-links/4uJnzYqu0jtJithJii_s0WXo9G0Ykvx3dvZas7bcy4E
```

Example response:

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "userId": "22222222-2222-2222-2222-222222222222",
  "title": "Example note",
  "content": "Example note content",
  "color": "#FFFFFF",
  "updatedAt": "2026-05-07T12:00:00",
  "createdAt": "2026-05-07T11:30:00"
}
```

## Health Checks

Custom endpoints:

```http
GET /healthz
GET /readyz
```

Actuator endpoints:

```http
GET /actuator/health
GET /actuator/health/liveness
GET /actuator/health/readiness
```

Examples through direct service access:

```bash
curl http://localhost:8083/healthz
curl http://localhost:8083/readyz
curl http://localhost:8083/actuator/health
curl http://localhost:8083/actuator/health/liveness
curl http://localhost:8083/actuator/health/readiness
```

## Environment Variables

```text
SPRING_APPLICATION_NAME=sharing-service
SPRING_PROFILES_ACTIVE=local
SERVER_PORT=8083

DB_HOST=postgres
DB_PORT=5432
POSTGRES_DB=notes_cloud
DB_USER=notes_cloud_user
DB_PASSWORD=notes_cloud_password

SPRING_JPA_HIBERNATE_DDL_AUTO=validate

NOTES_SERVICE_URL=http://notes-service:8082
SHARING_PUBLIC_BASE_URL=http://localhost:8080
```

For local development with port-forwarding:

```text
DB_HOST=localhost
DB_PORT=15432
POSTGRES_DB=notes_cloud
DB_USER=notes_cloud_user
DB_PASSWORD=notes_cloud_password

NOTES_SERVICE_URL=http://localhost:8082
SHARING_PUBLIC_BASE_URL=http://localhost:8080
```

## application.properties Example

```properties
spring.application.name=${SPRING_APPLICATION_NAME:sharing-service}
spring.profiles.active=${SPRING_PROFILES_ACTIVE:local}
server.port=${SERVER_PORT:8083}

spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:15432}/${POSTGRES_DB:notes_cloud}
spring.datasource.username=${DB_USER:notes_cloud_user}
spring.datasource.password=${DB_PASSWORD:notes_cloud_password}

spring.jpa.hibernate.ddl-auto=${SPRING_JPA_HIBERNATE_DDL_AUTO:validate}
spring.jpa.show-sql=${SPRING_JPA_SHOW_SQL:true}
spring.jpa.properties.hibernate.format_sql=${SPRING_JPA_FORMAT_SQL:true}

notes-service.url=${NOTES_SERVICE_URL:http://localhost:8082}
sharing.public-base-url=${SHARING_PUBLIC_BASE_URL:http://localhost:8080}

management.endpoints.web.exposure.include=health,info
management.endpoint.health.probes.enabled=true
management.health.readinessstate.enabled=true
management.health.livenessstate.enabled=true
management.endpoint.health.show-details=always
```

## Running Locally

From the service folder:

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

The service runs on:

```text
http://localhost:8083
```

## Local Testing With Cluster Database

Port-forward PostgreSQL:

```bash
kubectl port-forward -n notes-cloud svc/postgres 15432:5432
```

Port-forward Notes Service:

```bash
kubectl port-forward -n notes-cloud svc/notes-service 8082:8082
```

Then run Sharing Service locally from IntelliJ or with Maven.

## Testing From Postman

First check health directly:

```http
GET http://localhost:8083/healthz
GET http://localhost:8083/readyz
```

Create a share link through the gateway:

```http
POST http://localhost:8090/api/v1/users/{userId}/notes/{noteId}/share-links
```

Open the returned frontend link in the browser:

```http
GET http://localhost:8080/shared/{token}
```

Or test the API directly:

```http
GET http://localhost:8090/api/v1/share-links/{token}
```

If the note does not exist in Notes Service, opening the share link may return an error.

## Kubernetes

Apply ConfigMap, Secret, Service and Deployment manifests:

```bash
kubectl apply -f sharing-service-config.yaml
kubectl apply -f sharing-service-secret.yaml
kubectl apply -f sharing-service-service.yaml
kubectl apply -f sharing-service-deployment.yaml
```

Restart deployment:

```bash
kubectl rollout restart deployment sharing-service -n notes-cloud
kubectl rollout status deployment sharing-service -n notes-cloud
```

Check logs:

```bash
kubectl logs -n notes-cloud deployment/sharing-service
```

Port-forward the service for direct testing:

```bash
kubectl port-forward -n notes-cloud svc/sharing-service 8083:8083
```

## Important Notes

- The public share URL contains only a random token.
- The token is stored in the database together with `noteId`, `ownerId` and `expiresAt`.
- The link is view-only.
- The link expires automatically.
- Sharing Service owns share links and token validation.
- Notes Service owns the actual note content.
- Sharing API requests should usually go through the gateway at `http://localhost:8090/api/v1`.
- `SHARING_PUBLIC_BASE_URL` should point to the frontend public URL, not the internal Kubernetes service.
- For local cluster testing with NodePort, use `SHARING_PUBLIC_BASE_URL=http://localhost:8080`.
- `http://sharing-service:8083` works only inside Kubernetes-to-Kubernetes communication.
- For browser or Postman testing from your machine, use `http://localhost:8090/api/v1` through the gateway or `http://localhost:8083` with port-forwarding.
- Use `DB_USER` and `DB_PASSWORD`, not `POSTGRES_USER` and `POSTGRES_PASSWORD`, because the Kubernetes manifests provide `DB_USER` and `DB_PASSWORD`.
- For production, `SHARING_PUBLIC_BASE_URL` should be a real public frontend domain.
