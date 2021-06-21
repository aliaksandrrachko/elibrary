package by.it.academy.grodno.elibrary.main.server;

import by.it.academy.grodno.elibrary.main.configuration.ApplicationRootConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Define a class which would create, start and stop Tomcat Server.
 */
@Component
@Slf4j
public class EmbeddedTomcat {

    @Value("${tomcat.web.content.folder.path}")
    private String webContentFolder;
    @Value("${tomcat.web.base.folder.path}")
    private String webBaseFolder;
    @Value("${tomcat.port}")
    private Integer serverPort;

    private final Tomcat tomcat = new Tomcat();

    @PostConstruct
    public void start() throws ServletException, LifecycleException {
        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
        tomcat.setBaseDir(webBaseFolder);
        tomcat.setPort(serverPort);
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(".").getAbsolutePath());
        ctx.setParentClassLoader(ApplicationRootConfig.class.getClassLoader());
        tomcat.start();
        log.info("Tomcat Server Started at " + new Date());
        tomcat.getServer().await();
    }

    private String createTempDir() {
        try {
            File tempDir = File.createTempFile("tomcat.", "." + serverPort);
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir.getAbsolutePath();
        } catch (IOException ex) {
            throw new RuntimeException(
                    "Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
        }
    }

    @PreDestroy
    public void stop() throws LifecycleException {
        tomcat.stop();
    }
}
