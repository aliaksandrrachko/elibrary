package by.it.academy.grodno.elibrary.service.exceptions;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequestCode;

public class UnknownSubscriptionUpdateCodeRequest extends RuntimeException {

    private static final String UNKNOWN_STATUS_MESSAGE = "Unknown code for update subscription - '%s' for it request.";

    public UnknownSubscriptionUpdateCodeRequest() {
        super("Unknown request code for update subscription");
    }

    public UnknownSubscriptionUpdateCodeRequest(SubscriptionRequestCode code) {
        super(String.format(UNKNOWN_STATUS_MESSAGE, code));
    }

    public UnknownSubscriptionUpdateCodeRequest(String message) {
        super(message);
    }
}
