package by.it.academy.grodno.elibrary.api.utils.mail;

import by.it.academy.grodno.elibrary.entities.users.User;

import java.util.Map;

public interface IAGenericMessageTextCreator<E extends Enum<?>> {
    String createMessageText(E messageType, User user, Map<String, Object> attributes);
    Class<E> getGenericClass();
}