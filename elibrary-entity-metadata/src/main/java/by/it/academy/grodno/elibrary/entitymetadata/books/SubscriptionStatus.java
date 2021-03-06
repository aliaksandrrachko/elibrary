package by.it.academy.grodno.elibrary.entitymetadata.books;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SubscriptionStatus {
    BOOKING(1),
    READING(2),
    READING_EXTENDED(3),
    COMPLETED(4),
    READING_EXPIRED(5);

    private final int statusCode;

    SubscriptionStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    private static final Map<Integer, SubscriptionStatus> statusCodes;

    static {
        statusCodes = Stream.of(SubscriptionStatus.values())
                .collect(Collectors.toMap(SubscriptionStatus::getStatusCode, status -> status));
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static SubscriptionStatus getSubscriptionStatus(int statusCode){
        if (statusCodes.containsKey(statusCode)){
            return statusCodes.get(statusCode);
        } else {
            throw new IllegalArgumentException(String.format("Unknown status code - '%d'.", statusCode));
        }
    }
}
