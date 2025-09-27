FROM openjdk:24
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} tool-rent-backend.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "./tool-rent-backend.jar"]