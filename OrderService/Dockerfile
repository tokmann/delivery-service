FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY target/OrderService-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]