FROM openjdk:8-jdk-alpine
MAINTAINER Sameer
COPY target/hello-piper-0.0.1-SNAPSHOT.jar hello-piper-0.0.1.jar
ENTRYPOINT ["java","-jar","/hello-piper-0.0.1.jar"]