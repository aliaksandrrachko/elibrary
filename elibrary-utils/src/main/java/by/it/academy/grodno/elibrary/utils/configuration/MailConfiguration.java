package by.it.academy.grodno.elibrary.utils.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Properties;
import java.util.concurrent.Executor;

@Configuration
@ComponentScan("by.it.academy.grodno.elibrary.utils")
@EnableAsync
@Slf4j
public class MailConfiguration {

    private static final String ADMIN_EMAIL = "ssaannyyaa25@gmail.com";
    private static final String EMAIL_PASSWORD = "sanya252115";

    @Bean
    public JavaMailSender mailSender(@Value("${spring.mail.properties.mail.debug}") String mailDebugProperty,
                                     @Value("${spring.mail.properties.mail.smtp.auth}") String mailSmtpAuthProperty,
                                     @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String mailSmtpStarttlsEnableProperty,
                                     @Value("${spring.mail.properties.mail.transport.protocol}") String mailTransportProtocolProperty,
                                     @Value("${spring.mail.host}") String mailHostProperty,
                                     @Value("${spring.mail.port}") int mailPortProperty) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailHostProperty);
        mailSender.setPort(mailPortProperty);
        mailSender.setUsername(ADMIN_EMAIL);
        mailSender.setPassword(EMAIL_PASSWORD);

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

    @Async("threadPoolTaskExecutor")
    public void asyncMethodWithConfiguredExecutor() {
        log.info("Execute method with configured executor - {}", Thread.currentThread().getName());
    }
}
