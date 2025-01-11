FROM maven:3.8.7-eclipse-temurin-17 as builder

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -Dskiptests

FROM openjdk:17-jdk-slim-buster

WORKDIR /app

RUN apt-get update && \
    apt-get install -y curl iputils-ping postgresql-client

COPY --from=builder ./build/target/order-tracking-app-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["java", "-jar", "app.jar"]