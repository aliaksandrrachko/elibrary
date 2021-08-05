package by.it.academy.grodno.elibrary.api.jms;

public interface ReceivedJmsMessageHandler {

    void handleReceivedMessage(JmsMessage jmsMessage);
}
