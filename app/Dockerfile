FROM openjdk:17-jdk-slim
LABEL authors="mangobutter"
WORKDIR /app
COPY build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]