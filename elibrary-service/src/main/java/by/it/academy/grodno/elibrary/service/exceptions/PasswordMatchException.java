package by.it.academy.grodno.elibrary.service.exceptions;

import by.it.academy.grodno.elibrary.api.dto.UserDto;

public class PasswordMatchException extends RuntimeException {

    private static final String PASSWORD_MISMATCH = "Password mismatch for user with 'Username': '%s'";

    public PasswordMatchException(UserDto userDto) {
        super(String.format(PASSWORD_MISMATCH, userDto.getUsername()));
    }

    public PasswordMatchException() {
    }

    public PasswordMatchException(String message) {
        super(message);
    }

    public PasswordMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordMatchException(Throwable cause) {
        super(cause);
    }
}