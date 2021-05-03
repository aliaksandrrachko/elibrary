package by.it.academy.grodno.elibrary.api.utils.mail;

import by.it.academy.grodno.elibrary.entities.users.User;

public interface IEmailSender {

    void sendEmailToAdmin(User user, MailMessageType type);

    void sendEmailFromAdmin(User user, MailMessageType type);
}
