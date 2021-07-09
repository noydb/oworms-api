package com.power.service;

import com.power.dto.EmailDTO;
import com.power.dto.WordDTO;
import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import com.power.mail.MailContentBuilder;
import com.power.mail.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    private final MailProperties properties;

    @Autowired
    public EmailService(final JavaMailSender mailSender,
                        final MailContentBuilder mailContentBuilder,
                        final MailProperties properties) {
        this.mailSender = mailSender;
        this.mailContentBuilder = mailContentBuilder;
        this.properties = properties;
    }

    public void sendEmail(String title, String action, WordDTO wordDTO) {
        if (properties.isDisabled()) {
            return;
        }

        EmailDTO emailDTO = getEmailDTO(title, action, wordDTO);

        String[] recipients = properties.getRecipients().split(",");

        MimeMessagePreparator messagePrep = (MimeMessage mimeMessage) -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            messageHelper.setFrom("bot@oworms.com");
            messageHelper.setTo(recipients[0]);
            messageHelper.setSubject(title);
            messageHelper.setBcc(recipients);

            String messageContent = mailContentBuilder.build(emailDTO, EmailDTO.TEMPLATE);

            messageHelper.setText(messageContent, true);
        };

        try {
            mailSender.send(messagePrep);
        } catch (MailException e) {
            throw new OWormException(OWormExceptionType.EMAIL_SEND_FAILURE, "Failed to send new word email");
        }
    }

    private EmailDTO getEmailDTO(String title, String action, WordDTO wordDTO) {
        String retrievalLink = properties.getRetrievalLink().replace("{id}", String.valueOf(wordDTO.getId()));

        return new EmailDTO(title, action, wordDTO, retrievalLink);
    }
}
