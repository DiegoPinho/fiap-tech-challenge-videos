# Estágio de construção
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

# Estágio de execução
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar fiap-videos.jar
EXPOSE 8080
CMD ["java", "-jar", "fiap-videos.jar"]