package by.it.academy.grodno.elibrary.utils.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Base64;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class MailConfiguration {

    @Value("${spring.mail.username}") private String mailUsername;
    @Value("${spring.mail.password}") private String encoderPassword;
    @Value("${spring.mail.properties.mail.debug}") private String mailDebugProperty;
    @Value("${spring.mail.properties.mail.smtp.auth}") private String mailSmtpAuthProperty;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}") private String mailSmtpStarttlsEnableProperty;
    @Value("${spring.mail.properties.mail.transport.protocol}") private String mailTransportProtocolProperty;
    @Value("${spring.mail.host}") private String mailHostProperty;
    @Value("${spring.mail.port}") private int mailPortProperty;
    @Value("${spring.mail.default-encoding}") private String defaultEncoding;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailHostProperty);
        mailSender.setPort(mailPortProperty);
        mailSender.setUsername(mailUsername);
        mailSender.setDefaultEncoding(defaultEncoding);
        mailSender.setPassword(getDecryptPassword(encoderPassword));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnableProperty);
        javaMailProperties.put("mail.smtp.auth", mailSmtpAuthProperty);
        javaMailProperties.put("mail.transport.protocol", mailTransportProtocolProperty);
        javaMailProperties.put("mail.debug", mailDebugProperty);

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    private static String getDecryptPassword(String data){
        return new String(Base64.getDecoder().decode(data));
    }

    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");
        return messageSource;
    }

    @Value("${input.encoding}")
    private String emailTemplateEncoding;

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(2);
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(emailTemplateEncoding);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
