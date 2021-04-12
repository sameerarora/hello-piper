#Package stage
FROM openjdk:8-jdk-alpine
COPY target/hello-piper-*.jar hello-piper.jar
EXPOSE 8085

ENTRYPOINT ["java","-Dserver.port=8085","-jar","/hello-piper.jar"]
