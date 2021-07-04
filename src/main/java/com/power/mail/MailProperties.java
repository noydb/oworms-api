package com.power.mail;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("mail")
@PropertySource(value = "classpath:mail.${spring.profiles.active}.properties")
@Data
public class MailProperties {
    private String username;
    private String password;
    private String host;
    private String port;
    private String transportProtocol;
    private String smtpAuth;
    private String smtpStarttlsEnable;
    private String debug;

    private String retrievalLink;
}
