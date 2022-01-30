package com.power.oworms.mail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("mail")
@PropertySource(value = "classpath:mail.${spring.profiles.active}.properties")
public class MailProperties {
    private boolean disabled;
    private String username;
    private String password;
    private String retrievalLink;
    private String eatBananaLink;
    private String recipients;
    private String host;
    private String port;
    private String transportProtocol;
    private String smtpAuth;
    private String smtpStarttlsEnable;
    private String debug;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetrievalLink() {
        return retrievalLink;
    }

    public void setRetrievalLink(String retrievalLink) {
        this.retrievalLink = retrievalLink;
    }

    public String getEatBananaLink() {
        return eatBananaLink;
    }

    public void setEatBananaLink(String eatBananaLink) {
        this.eatBananaLink = eatBananaLink;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getSmtpStarttlsEnable() {
        return smtpStarttlsEnable;
    }

    public void setSmtpStarttlsEnable(String smtpStarttlsEnable) {
        this.smtpStarttlsEnable = smtpStarttlsEnable;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }
}
