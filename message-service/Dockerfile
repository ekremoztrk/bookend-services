FROM maven:3.8.1-jdk-11 AS build

MAINTAINER ekrem

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/message-service-0.0.1-SNAPSHOT.jar /usr/local/lib/message-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/usr/local/lib/message-service-0.0.1-SNAPSHOT.jar"]
