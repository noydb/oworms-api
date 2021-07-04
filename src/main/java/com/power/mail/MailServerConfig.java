package com.power.mail;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Data
@Import({MailProperties.class})
public class MailServerConfig {

    @Bean
    public JavaMailSender getJavaMailSender(MailProperties properties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(properties.getHost());
        mailSender.setPort(Integer.parseInt(properties.getPort()));

        mailSender.setUsername(properties.getUsername());
        mailSender.setPassword(properties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", properties.getTransportProtocol());
        props.put("mail.smtp.auth", properties.getSmtpAuth());
        props.put("mail.smtp.starttls.enable", properties.getSmtpStarttlsEnable());
        props.put("mail.debug", properties.getDebug());

        return mailSender;
    }

}
