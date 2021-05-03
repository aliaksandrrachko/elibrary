package by.it.academy.grodno.elibrary.utils.factories;

import by.it.academy.grodno.elibrary.api.utils.mail.IMessageTextCreator;
import by.it.academy.grodno.elibrary.api.utils.mail.MailMessageType;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.stereotype.Service;

@Service
public class MessageTextCreatorThymeleafImpl implements IMessageTextCreator {

    @Override
    public String createMessageText(MailMessageType mailMessageType, User user) {
        return null;
    }
}
