package by.it.academy.grodno.elibrary.api.exceptions;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequestCode;

public class UnknownSubscriptionUpdateCodeRequestException extends RuntimeException {

    private static final String UNKNOWN_STATUS_MESSAGE = "Unknown code for update subscription - '%s' for it request.";

    public UnknownSubscriptionUpdateCodeRequestException() {
        super("Unknown request code for update subscription");
    }

    public UnknownSubscriptionUpdateCodeRequestException(SubscriptionRequestCode code) {
        super(String.format(UNKNOWN_STATUS_MESSAGE, code));
    }

    public UnknownSubscriptionUpdateCodeRequestException(String message) {
        super(message);
    }
}
