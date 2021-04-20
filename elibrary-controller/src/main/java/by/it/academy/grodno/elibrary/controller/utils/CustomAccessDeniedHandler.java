package by.it.academy.grodno.elibrary.controller.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        // this method do some event when throw AccessDeniedException
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            log.info("User {} doesn't have permission!", authentication.getName());
        }
        response.sendRedirect(request.getContextPath() + "/403");
    }
}