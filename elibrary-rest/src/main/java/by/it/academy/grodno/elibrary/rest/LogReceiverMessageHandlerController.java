package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.jms.JmsMessage;
import by.it.academy.grodno.elibrary.jms.sender.SimpleJmsMessageSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/jms")
public class LogReceiverMessageHandlerController {

    private final SimpleJmsMessageSender jmsMessageSender;

    public LogReceiverMessageHandlerController(SimpleJmsMessageSender jmsMessageSender) {
        this.jmsMessageSender = jmsMessageSender;
    }

    @GetMapping(value = "/to/{to}/body/{body}")
    public void printToLogJmsMessage(@PathVariable String to, @PathVariable String body) {
        jmsMessageSender.sendJmsMessage(new JmsMessage(to, body));
    }
}
