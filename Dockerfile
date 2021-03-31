#Build stage
FROM openjdk:8-jdk-alpine as build
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src src
RUN ./mvnw package -DskipTests

#Package stage
FROM openjdk:8-jdk-alpine
COPY --from=build target/hello-piper-0.0.1-SNAPSHOT.jar hello-piper-0.0.1.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar","/hello-piper-0.0.1.jar"]