FROM openjdk:17-jdk-slim
LABEL authors="mangobutter"
WORKDIR /admin
COPY build/libs/*SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]