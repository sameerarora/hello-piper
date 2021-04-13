#Package stage
FROM openjdk:8-jdk-alpine
COPY target/hello-piper-*.jar hello-piper.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/hello-piper.jar"]