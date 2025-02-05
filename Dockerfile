FROM maven:3.9.9-eclipse-temurin-21-alpine as build

COPY src src
COPY pom.xml pom.xml

RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-debian:21

WORKDIR /app

COPY --from=build target/TaskManagementSystem-1.0-SNAPSHOT.jar ./application.jar

ENTRYPOINT ["java", "-jar", "./application.jar"]
