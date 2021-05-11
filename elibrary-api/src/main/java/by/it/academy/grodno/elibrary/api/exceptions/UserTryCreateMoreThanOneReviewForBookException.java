package by.it.academy.grodno.elibrary.api.exceptions;

public class UserTryCreateMoreThanOneReviewForBookException extends RuntimeException{

    private static final String MESSAGE_PATTERN =
            "User with id %d trying create more than one review for book with id '%d'.";

    private final Long userId;
    private final Long bookId;

    public UserTryCreateMoreThanOneReviewForBookException(Long userId, Long bookId) {
        super(String.format(MESSAGE_PATTERN, userId, userId));
        this.userId = userId;
        this.bookId = bookId;

    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }
}
