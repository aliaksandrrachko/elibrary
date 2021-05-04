package by.it.academy.grodno.elibrary.utils.factories;

import by.it.academy.grodno.elibrary.api.utils.mail.IAGenericMessageTextCreator;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.context.i18n.LocaleContextHolder;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

public abstract class AGenericMessageTextCreator<E extends Enum<?>> implements IAGenericMessageTextCreator<E> {

    protected final ITemplateEngine templateEngine;
    protected final Locale locale;
    private final Class<E> clazz;

    public AGenericMessageTextCreator(ITemplateEngine templateEngine, Class<E> clazz) {
        this.templateEngine = templateEngine;
        this.locale = LocaleContextHolder.getLocale();
        this.clazz = clazz;
    }

    @Override
    public String createMessageText(E messageType, User user, Map<String, Object> attributes) {
        final Context ctx = new Context();
        ctx.setVariable("user", user);
        ctx.setLocale(locale);
        setAttributeToContextIfExists(ctx, attributes);
        String template = getTemplate(messageType);
        return templateEngine.process(template, ctx);
    }

    @Override
    public Class<E> getGenericClass() {
        return this.clazz;
    }

    protected abstract String getTemplate(E messageType);

    private void setAttributeToContextIfExists(Context ctx, Map<String, Object> attributes){
        if (attributes != null && !attributes.isEmpty()){
            for(Map.Entry<String, Object> entry : attributes.entrySet()){
                ctx.setVariable(entry.getKey(), entry.getValue());
            }
        }
    }
}
