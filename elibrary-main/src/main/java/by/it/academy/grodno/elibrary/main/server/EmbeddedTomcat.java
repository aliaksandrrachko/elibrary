package by.it.academy.grodno.elibrary.main.server;

import by.it.academy.grodno.elibrary.main.ElibraryApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

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
    public void start() throws ServletException, LifecycleException, IOException {
        System.setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");
        StopWatch clock = new StopWatch(tomcat.toString());
        clock.start(tomcat.toString());
        log.info("Tomcat StartUp {}.", LocalDateTime.now());
        tomcat.setBaseDir(createTempDir());
        tomcat.setPort(serverPort);
        tomcat.getHost().setAppBase(webBaseFolder);
        Context context = tomcat.addWebapp("", webContentFolder);
        context.setParentClassLoader(ElibraryApplication.class.getClassLoader());
        tomcat.start();
        log.info("Tomcat Server Started at {} ms.", clock.getTotalTimeMillis());
        tomcat.getServer().await();
    }

    private String createTempDir() throws IOException {
        try {
            File tempDir = File.createTempFile("tomcat.", "." + serverPort);
            Files.deleteIfExists(tempDir.toPath());
            Files.createDirectory(tempDir.toPath());
            tempDir.deleteOnExit();
            return tempDir.getAbsolutePath();
        } catch (IOException ex) {
            log.warn("Unable to create tempDir", ex);
            throw new IOException(
                    String.format( "Unable to create tempDir. java.io.tmpdir is set to %s",
                            System.getProperty("java.io.tmpdir")),
                    ex);
        }
    }

    @PreDestroy
    public void stop() throws LifecycleException {
        tomcat.stop();
        log.info("Tomcat Server Stop at {}", LocalDateTime.now());
    }
}
