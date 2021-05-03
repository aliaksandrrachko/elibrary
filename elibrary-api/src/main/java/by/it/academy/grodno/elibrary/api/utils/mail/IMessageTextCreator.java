package by.it.academy.grodno.elibrary.api.utils.mail;

import by.it.academy.grodno.elibrary.entities.users.User;

public interface IMessageTextCreator {

    String createMessageText(MailMessageType mailMessageType, User user);
}
