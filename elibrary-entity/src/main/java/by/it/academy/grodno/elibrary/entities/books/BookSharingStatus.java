package by.it.academy.grodno.elibrary.entities.books;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BookSharingStatus {
    BOOKING(1),
    READING(2),
    PROROGUE(3),
    COMPLETED(4);

    private final int statusCode;

    BookSharingStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    private static final Map<Integer, BookSharingStatus> statusCodes;

    static {
        statusCodes = Stream.of(BookSharingStatus.values())
                .collect(Collectors.toMap(BookSharingStatus::getStatusCode, status -> status));

    }

    public int getStatusCode() {
        return statusCode;
    }

    public static BookSharingStatus getBookSharingStatus(int statusCode){
        if (statusCodes.containsKey(statusCode)){
            return statusCodes.get(statusCode);
        } else {
            throw new IllegalArgumentException(String.format("Unknown status code - '%d'.", statusCode));
        }
    }
}
