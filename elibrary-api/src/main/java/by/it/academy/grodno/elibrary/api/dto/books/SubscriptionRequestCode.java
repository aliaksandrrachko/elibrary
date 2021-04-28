package by.it.academy.grodno.elibrary.api.dto.books;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SubscriptionRequestCode {
    BOOKING(1),
    TAKE_BOOK(2),
    LEAVE_BOOK(3),
    EXTENDED_SUBSCRIPTION(4),
    UNDO_BOOKING(5);

    private final int requestCode;

    SubscriptionRequestCode(int statusCode) {
        this.requestCode = statusCode;
    }

    private static final Map<Integer, SubscriptionRequestCode> statusCodes;

    static {
        statusCodes = Stream.of(SubscriptionRequestCode.values())
                .collect(Collectors.toMap(SubscriptionRequestCode::getRequestCode, status -> status));
    }

    public int getRequestCode() {
        return requestCode;
    }

    public static SubscriptionRequestCode getSubscriptionRequestCode(int statusCode){
        if (statusCodes.containsKey(statusCode)){
            return statusCodes.get(statusCode);
        } else {
            throw new IllegalArgumentException(String.format("Unknown request code - '%d'.", statusCode));
        }
    }
}
