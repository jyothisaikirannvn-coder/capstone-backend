# ---- Build Stage (Java 21) ----
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

# ---- Run Stage (Java 21) ----
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# ✅ SAME AS REGISTER-BACKEND (WORKS!)
EXPOSE 8080

# ✅ CRITICAL: Render $PORT
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]