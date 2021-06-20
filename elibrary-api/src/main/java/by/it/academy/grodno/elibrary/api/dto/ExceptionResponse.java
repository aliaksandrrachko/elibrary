package by.it.academy.grodno.elibrary.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse implements Serializable {

    private String message;
    private LocalDateTime timeStamp;
    private Exception exception;
    private StackTraceElement[] stackTrace;
}
