package by.it.academy.grodno.elibrary.main;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcDispatcherServletInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ELibraryApplicationRootConfig.class};
    }

    @NotNull
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}