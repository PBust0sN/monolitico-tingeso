FROM maven:3.9.9-amazoncorretto-17 AS build

WORKDIR /app

COPY ./pom.xml .
COPY ./src ./src

RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests

FROM openjdk:24

WORKDIR /app

COPY --from=build /app/target/*.jar tool-rent-backend.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "./tool-rent-backend.jar"]