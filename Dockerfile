FROM openjdk:17-jdk-alpine
LABEL authors="Timothy Olisaeloka Ngonadi"
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
CMD ["java", "-jar", "app.jar"]