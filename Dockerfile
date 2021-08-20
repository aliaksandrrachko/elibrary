#INSTRUCTION arguments
FROM openjdk:8-jdk-alpine
ARG CONT_IMG_VER
ENV CONT_IMG_VER=v1.0.0
RUN echo $CONT_IMG_VER
VOLUME /tmp
EXPOSE 8080 8080
# EXPOSE <port> [<port>/<protocol>...]
ARG JAR_FILE=elibrary-main/target/elibrary-main-0.0.1-SNAPSHOT.jar
# directive=value
LABEL description="The LABEL instruction adds metadata to an image. \
That label-values can span multiple lines."
WORKDIR /opt/elibrary
# The WORKDIR instruction sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions that follow it in the Dockerfile
# ADD [--chown=<user>:<group>] ["<src>",... "<dest>"]
# The ADD instruction copies new files, directories or remote file URLs from <src> and adds them to the filesystem of the image at the path <dest>.
ONBUILD RUN echo "on build run echo"
COPY ${JAR_FILE} elibrary-spring-boot.jar
# COPY [--chown=<user>:<group>] ["<src>",... "<dest>"]
# The COPY instruction copies new files or directories from <src> and adds them to the filesystem of the container at the path <dest>.
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "elibrary-spring-boot.jar"]
# ENTRYPOINT ["executable", "param1", "param2"]
# An ENTRYPOINT allows you to configure a container that will run as an executable.
# Both CMD and ENTRYPOINT instructions define what command gets executed when running a container. There are few rules that describe their co-operation.
# Dockerfile should specify at least one of CMD or ENTRYPOINT commands.
# ENTRYPOINT should be defined when using the container as an executable.
# CMD should be used as a way of defining default arguments for an ENTRYPOINT command or for executing an ad-hoc command in a container.
# CMD will be overridden when running the container with alternative arguments.
HEALTHCHECK --start-period=30s --interval=30s --timeout=3s --retries=3 \
            CMD curl --silent --fail --request GET http://localhost:8080/actuator/health \
                | jq --exit-status '.status == "UP"' || exit 1