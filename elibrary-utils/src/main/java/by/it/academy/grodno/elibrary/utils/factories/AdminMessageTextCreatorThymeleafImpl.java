package by.it.academy.grodno.elibrary.utils.factories;

import by.it.academy.grodno.elibrary.api.utils.mail.AdminMailMessageType;
import by.it.academy.grodno.elibrary.api.utils.mail.IAdminMessageTextCreator;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;

@Service
public class AdminMessageTextCreatorThymeleafImpl extends AGenericMessageTextCreator<AdminMailMessageType> implements IAdminMessageTextCreator {

    public AdminMessageTextCreatorThymeleafImpl(ITemplateEngine templateEngine) {
        super(templateEngine, AdminMailMessageType.class);
    }

    @Override
    protected String getTemplate(AdminMailMessageType messageType) {
        if (messageType == AdminMailMessageType.SUBSCRIPTION_EXPIRED_INFO) {
            return "mailtemplates/admin/subscriptionExpiredInfo.html";
        }
        throw new IllegalStateException("Unexpected value: " + messageType);
    }
}
