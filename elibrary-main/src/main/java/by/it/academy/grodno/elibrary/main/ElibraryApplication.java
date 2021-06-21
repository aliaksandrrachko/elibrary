package by.it.academy.grodno.elibrary.main;

import by.it.academy.grodno.elibrary.main.server.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Define a class which would launch The Application and initialize
 * {@link by.it.academy.grodno.elibrary.main.server.EmbeddedTomcat} server.
 */
@Slf4j
public class ElibraryApplication {

    public static void main(String[] ars) {
        AnnotationConfigApplicationContext ctx = null;
        try {
            ctx = new AnnotationConfigApplicationContext();
            ctx.register(ServerConfig.class);
            ctx.refresh();
            ctx.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
