package by.it.academy.grodno.elibrary.rest.exceptionhandlers;

import by.it.academy.grodno.elibrary.api.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception ex){
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .exception(ex)
                        .timeStamp(LocalDateTime.now().withNano(0))
                        .message(ex.getMessage())
                        .stackTrace(ex.getStackTrace()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
