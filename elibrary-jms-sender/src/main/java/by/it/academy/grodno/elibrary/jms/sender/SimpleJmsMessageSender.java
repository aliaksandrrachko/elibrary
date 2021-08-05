package by.it.academy.grodno.elibrary.jms.sender;

import by.it.academy.grodno.elibrary.api.jms.JmsMessage;
import by.it.academy.grodno.elibrary.api.jms.JmsMessageSender;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SimpleJmsMessageSender implements JmsMessageSender {

    private final JmsTemplate jmsTemplate;

    public SimpleJmsMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendJmsMessage(JmsMessage jmsMessage) {
        jmsTemplate.convertAndSend("mailbox", jmsMessage);
    }
}
