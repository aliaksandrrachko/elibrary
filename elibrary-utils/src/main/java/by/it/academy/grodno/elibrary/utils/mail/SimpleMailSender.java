package by.it.academy.grodno.elibrary.utils.mail;

import by.it.academy.grodno.elibrary.api.utils.mail.*;
import by.it.academy.grodno.elibrary.entities.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

//@Service
@Slf4j
public class SimpleMailSender implements IEmailSender {

    @Value("${spring.mail.send}")
    private boolean sendMail;

    @Value("${spring.mail.username}")
    private String adminMail;

    private static final String SUBJECT = "E-Library";

   // private final JavaMailSender mailSender;
    private final IUserMessageTextCreator userMessageTextCreator;
    private final IAdminMessageTextCreator adminMessageTextCreator;

    public SimpleMailSender(//@Qualifier("applicationMailSender") JavaMailSender mailSender,
                            IUserMessageTextCreator userMessageTextCreator,
                            IAdminMessageTextCreator adminMessageTextCreator) {
        //this.mailSender = mailSender;
        this.userMessageTextCreator = userMessageTextCreator;
        this.adminMessageTextCreator = adminMessageTextCreator;
    }

    @Async
    @Override
    public void sendEmailToAdmin(User user, AdminMailMessageType type, Map<String, Object> attributes) {
/*        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String text = adminMessageTextCreator.createMessageText(type, user, attributes);
        String subject = SUBJECT + " [" + type + "]";
        configureMimeMessageHelper(helper, adminMail, adminMail, text, subject);
        if (sendMail) {
            mailSender.send(message);
        }*/
    }

    @Async
    @Override
    public void sendEmailFromAdmin(User user, UserMailMessageType type, Map<String, Object> attributes) {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//        String text = userMessageTextCreator.createMessageText(type, user, attributes);
//        configureMimeMessageHelper(helper, adminMail, user.getEmail(), text, SUBJECT);
//        if (sendMail) {
//            mailSender.send(message);
//        }
    }
//
//    private void configureMimeMessageHelper(MimeMessageHelper helper, String mailFrom, String mailTo,
//                                            String mailText, String mailSubject) {
//        try {
//            helper.setFrom(mailFrom);
//            helper.setTo(mailTo);
//            helper.setText(mailText, true);
//            helper.setSubject(mailSubject);
//        } catch (MessagingException e) {
//            log.error(e.getMessage());
//        }
//    }
}
