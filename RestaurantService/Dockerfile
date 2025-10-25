FROM openjdk:23-jdk
WORKDIR /app
COPY target/RestaurantService-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]