package by.it.academy.grodno.elibrary.api.utils.mail;

import by.it.academy.grodno.elibrary.entities.users.User;

import java.util.Map;

public interface IEmailSender {
    void sendEmailToAdmin(User user, AdminMailMessageType type, Map<String, Object> attributes);
    void sendEmailFromAdmin(User user, UserMailMessageType type, Map<String, Object> attributes);
}
