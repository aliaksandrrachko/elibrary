package by.it.academy.grodno.elibrary.utils.mail;

import by.it.academy.grodno.elibrary.api.utils.mail.IEmailSender;
import by.it.academy.grodno.elibrary.api.utils.mail.MailMessageType;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.stereotype.Service;

@Service
public class MailSender implements IEmailSender {


    @Override
    public void sendEmailToAdmin(User user, MailMessageType type) {

    }

    @Override
    public void sendEmailFromAdmin(User user, MailMessageType type) {

    }
/*
    private static final String ADMIN_EMAIL_ADDRESS = "ssaannyyaa25@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    @Qualifier(value = "messageTextCreatorVelocityImpl")
    private IMessageTextCreator messageTextCreator;

    @Async
    @Override
    public void sendEmailToAdmin(UserDto dto, MailMessageType type) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userDto", dto);
        String text = messageTextCreator.createMessageText(type, map);
        String subject = "User: " + dto.getUsername() + " update account!";
        configureMimeMessageHelper(helper, ADMIN_EMAIL_ADDRESS, ADMIN_EMAIL_ADDRESS, text, subject);
        mailSender.send(message);
    }

    @Async
    @Override
    public void sendEmailFromAdmin(UserDto userDto, MailMessageType type) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userDto", userDto);
        String text = messageTextCreator.createMessageText(type, map);
        configureMimeMessageHelper(helper, ADMIN_EMAIL_ADDRESS, userDto.getEmail(), text, "You registered in The Final Project");
        mailSender.send(message);
    }

    private void configureMimeMessageHelper(MimeMessageHelper helper,
                                            String mailFrom, String mailTo, String mailText,
                                            String mailSubject) throws MessagingException {
        helper.setFrom(mailFrom);
        helper.setTo(mailTo);
        helper.setText(mailText, true);
        helper.setSubject(mailSubject);
    }*/
}
