FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=elibrary-main/target/elibrary-main-0.0.1-SNAPSHOT.jar
WORKDIR /opt/elibrary
COPY ${JAR_FILE} elibrary-spring-boot.jar
ENTRYPOINT ["java","-jar","elibrary-spring-boot.jar"]