package by.it.academy.grodno.elibrary.utils.factories;

import by.it.academy.grodno.elibrary.api.utils.mail.IUserMessageTextCreator;
import by.it.academy.grodno.elibrary.api.utils.mail.UserMailMessageType;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;

@Service
public class UserMessageTextCreatorThymeleafImpl extends AGenericMessageTextCreator<UserMailMessageType> implements IUserMessageTextCreator {

    public UserMessageTextCreatorThymeleafImpl(ITemplateEngine templateEngine) {
        super(templateEngine, UserMailMessageType.class);
    }

    @Override
    protected String getTemplate(UserMailMessageType messageType) {
        switch (messageType) {
            case REGISTERED:
            case REGISTERED_WITH_PASSWORD:
                return "mailtemplates/user/userRegistered.html";
            case USER_BOOKING_BOOK:
                return "mailtemplates/user/userBookingBook.html";
            case SUBSCRIPTION_EXPIRED:
                return "mailtemplates/user/subscriptionExpired.html";
            case BOOKING_WAS_UNDO:
                return "mailtemplates/user/bookingUndo.html";
            default:
                throw new IllegalStateException("Unexpected value: " + messageType);
        }
    }
}
