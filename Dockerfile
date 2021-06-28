FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8085
ARG JAR_FILE=elibrary-main/target/elibrary-main-0.0.1-SNAPSHOT-jar-with-dependencies.jar
WORKDIR /opt/elibrary
COPY ${JAR_FILE} elibrary-spring-framework.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","elibrary-spring-framework.jar"]