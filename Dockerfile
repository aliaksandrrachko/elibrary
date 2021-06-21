FROM tomcat:9.0.48-jdk8-adoptopenjdk-hotspot
VOLUME /tmp
ARG WAR_FILE=elibrary-main/target/elibrary-main-0.0.1-SNAPSHOT.war
## COPY ${WAR_FILE} /usr/local/tomcat/webapps/

ADD elibrary-main/src/main/webapp/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
ADD elibrary-main/src/main/webapp/context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY ${WAR_FILE} /usr/local/tomcat/webapps/
## ADD build/libs/spring-war-with-docker-tomcat-1.0.war /usr/local/tomcat/webapps/

RUN ln -s /usr/local/tomcat/webapps.dist/manager webapps/manager
RUN ln -s /usr/local/tomcat/webapps.dist/host-manager webapps/host-manager
RUN ln -s /usr/local/tomcat/webapps.dist/ROOT webapps/ROOT

EXPOSE 8080

## CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
CMD ["catalina.sh", "run"]