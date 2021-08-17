FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8085
ARG JAR_FILE=elibrary-main/target/elibrary-main-0.0.1-SNAPSHOT-jar-with-dependencies.jar
ARG APPLICATION_PROPERITES_FILE=elibrary-main/src/main/resources/application.properties
ARG SERVER_PROPERTIES_FILE=elibrary-main/src/main/resources/server.properties
WORKDIR /opt/elibrary
COPY ${JAR_FILE} elibrary-spring-framework.jar
COPY ${APPLICATION_PROPERITES_FILE} application.properties
COPY ${SERVER_PROPERTIES_FILE} server.properties
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "elibrary-spring-framework.jar"]