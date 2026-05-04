# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first for better Docker layer caching
COPY sharing-service/pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY sharing-service/src src

# Build jar
RUN mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]