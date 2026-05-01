# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY sharing-service/pom.xml .
COPY sharing-service/.mvn .mvn
COPY sharing-service/mvnw .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY sharing-service/src src

RUN ./mvnw clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]