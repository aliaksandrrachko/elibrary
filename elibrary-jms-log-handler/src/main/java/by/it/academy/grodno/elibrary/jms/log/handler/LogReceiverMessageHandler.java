package by.it.academy.grodno.elibrary.jms.log.handler;

import by.it.academy.grodno.elibrary.api.jms.JmsMessage;
import by.it.academy.grodno.elibrary.api.jms.ReceivedJmsMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogReceiverMessageHandler implements ReceivedJmsMessageHandler {

    @Override
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void handleReceivedMessage(JmsMessage jmsMessage) {
        log.info("Received <" + jmsMessage + ">");
    }
}
